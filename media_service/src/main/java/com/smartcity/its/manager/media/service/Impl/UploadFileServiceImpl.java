package com.smartcity.its.manager.media.service.Impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.its.module.model.exception.BadRequestException;
import com.its.module.model.response.Response;
import com.smartcity.its.manager.media.MediaConfig;
import com.smartcity.its.manager.media.common.FileUtils;
import com.smartcity.its.manager.media.common.NameUtils;
import com.smartcity.its.manager.media.common.Enum.FileType;
import com.smartcity.its.manager.media.dto.FileProperties;
import com.smartcity.its.manager.media.dto.FileUploadReponse;
import com.smartcity.its.manager.media.service.FileStorageService;
import com.smartcity.its.manager.media.service.UploadFileService;

import net.coobird.thumbnailator.Thumbnails;

@DependsOn("MediaConfig")
@Service("UploadFileService")
public class UploadFileServiceImpl implements UploadFileService {

	@Autowired
	private MediaConfig mediaConfig;

	private FileStorageService imageStore;
	private FileStorageService videoStore;
	private FileStorageService documentStore;

	@PostConstruct
	public void init() {
		this.imageStore = new FileStorageService(mediaConfig.getImageDir());
		this.videoStore = new FileStorageService(mediaConfig.getVideoDir());
		this.documentStore = new FileStorageService(mediaConfig.getDocumentDir());
	}

	@Override
	public Response<FileUploadReponse> uploadFiles(MultipartFile[] files) {
		validateSupportFiles(files, mediaConfig.getSupportTypes());
		List<FileProperties> fileProperties = new ArrayList<>();
		for (MultipartFile file : files) {
			String originalName = StringUtils.cleanPath(file.getOriginalFilename());
			String fileExtension = NameUtils.getFileExtension(originalName);
			String fileName = NameUtils.randomUUIDName() + "." + fileExtension;

			FileProperties storedFile = storeFile(file, fileName);
			storedFile.setOriginalName(originalName);
			storedFile.setFileExtension(fileExtension);
			fileProperties.add(storedFile);
		}
		FileUploadReponse fileUploadReponse = new FileUploadReponse(fileProperties);
		return Response.<FileUploadReponse>builder().code(200).message("ok").data(fileUploadReponse).build();
	}

	private String createThumbnailFromImg(String fileName) {
		if (fileName.endsWith("svg"))
			return null;
		String outputFile = mediaConfig.getImageDir() + "/thumbnails/" + NameUtils.getFileName(fileName) + ".jpg";
		try {
			String inputPath = Paths.get(mediaConfig.getImageDir() + "/" + fileName).toAbsolutePath().normalize()
					.toString();
			String outputPath = Paths.get(outputFile).toAbsolutePath().normalize().toString();
			File output = new File(outputPath);
			Thumbnails.of(new File(inputPath)).size(mediaConfig.getThumbnailSize(), mediaConfig.getThumbnailSize())
					.outputFormat("jpg").toFile(output);
			return outputFile.substring(outputFile.indexOf('/'));
		} catch (IOException e) {
			// LogUtils.info(this.getClass(), e);
			e.printStackTrace();
			return null;
		}
	}

//	private String createThumbnailFromVideo(String inputFile) {
//		String inputPath = Paths.get(mediaConfig.getVideoDir() + "/" + inputFile).toAbsolutePath().normalize()
//				.toString();
//		String outputFile = mediaConfig.getVideoDir() + "/thumbnails/" + NameUtils.getFileName(inputFile) + ".jpg";
//		String outputPath = Paths.get(outputFile).toAbsolutePath().normalize().toString();
//		try {
//			File input = new File(inputPath);
//			SeekableByteChannel bc = NIOUtils.readableChannel(input);
//			MP4Demuxer dm = MP4Demuxer.createMP4Demuxer(bc);
//			DemuxerTrack vt = dm.getVideoTrack();
//			double totalSec = vt.getMeta().getTotalDuration();
//			bc = NIOUtils.readableChannel(input);
//			FrameGrab grab = FrameGrab.createFrameGrab(bc);
//			grab.seekToSecondPrecise(totalSec / 2);
//			Picture picture = grab.getNativeFrame();
//			BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
//			ImageIO.write(bufferedImage, "jpg", new File(outputPath));
//			dm.close();
//			bc.close();
//			return outputFile.substring(outputFile.indexOf('/'));
//		} catch (Exception e) {
//			e.printStackTrace();
//			//LogUtils.info(this.getClass(), e);
//			return "Codec not support!";
//		}
//	}

	private void validateSupportFiles(MultipartFile[] files, String[] supportTypes) {
		if (files == null || files.length == 0)
			throw new BadRequestException("files field can not be empty!");
		for (MultipartFile file : files) {
			if (!FileUtils.validateSupportFile(file, supportTypes))
				throw new BadRequestException(file.getOriginalFilename() + " is not supported!");
		}
	}

	@Override
	public Response<Object> deleteFile(String[] filePaths) {
		List<String> fail = new ArrayList<String>();
		List<String> success = new ArrayList<String>();

		for (String filePath : filePaths) {
			String inputPath = Paths.get("data" + filePath).toAbsolutePath().normalize().toString();
			File input = new File(inputPath);
			if (!input.delete())
				fail.add(filePath);
			else {
				success.add(filePath);
				int index = filePath.indexOf("/", 2) + 1;
				inputPath = Paths
						.get("data" + filePath.substring(0, index) + "thumbnails/"
								+ filePath.substring(index, filePath.lastIndexOf(".")) + ".jpg")
						.toAbsolutePath().normalize().toString();
				input = new File(inputPath);
				input.delete();
			}
		}

		Map<String, Object> ans = new HashMap<String, Object>();
		ans.put("success", success);
		ans.put("fail", fail);

		return Response.builder().code(200).message("ok").data(ans).build();
	}

	private FileProperties storeFile(MultipartFile file, String fileName) {
		try {
			FileProperties fileProperties = new FileProperties();
			String location = "";
			FileType fileType = FileUtils.getFileType(file);
			String thumbnail = "";
			switch (fileType) {
			case IMAGE_FILE:
				imageStore.storeFile(file.getInputStream(), fileName);
				location = location + "/images/";
				thumbnail = createThumbnailFromImg(fileName);
				break;
//				case VIDEO_FILE:
//					videoStore.storeFile(file.getInputStream(), fileName);
//					location = location + "/videos/";
//					thumbnail = createThumbnailFromVideo(fileName);
//					break;
			case DOCUMENT_FILE:
				documentStore.storeFile(file.getInputStream(), fileName);
				location = location + "/documents/";
				break;
			default:
				break;
			}

			fileProperties.setRelLocation(location + fileName);
			fileProperties.setFileType(fileType.toString());
			fileProperties.setHost(mediaConfig.getMediaHost());
			fileProperties.setSize(file.getSize());
			fileProperties.setThumbnail(thumbnail);
			return fileProperties;
		} catch (IOException e) {
			e.printStackTrace();
			throw new BadRequestException("Ghi gile thất bại");
		}
	}

	@Override
	public Response<?> uploadImageBase64(String imageString) {
		try {
			String contentType = imageString.split(";")[0];
			String fileExtension = null;
			switch (contentType) {
			case "data:image/png":
				fileExtension = "png";
				break;
			case "data:image/jpeg":
				fileExtension = "jpeg";
				break;
			default:
				fileExtension = "jpg";
			}
			String fileName = NameUtils.randomUUIDName() + "." + fileExtension;
			byte[] imageByte;
			// BASE64Decoder decoder = new BASE64Decoder();
			imageByte = Base64.getDecoder().decode(imageString);
			InputStream is = new ByteArrayInputStream(imageByte);
			FileProperties storedFile = new FileProperties();
			imageStore.storeFile(is, fileName);
			storedFile.setOriginalName("image." + fileExtension);
			storedFile.setFileExtension(fileExtension);

			storedFile.setRelLocation("/images/" + fileName);
			storedFile.setFileType(FileType.IMAGE_FILE.toString());
			storedFile.setHost(mediaConfig.getMediaHost());
			// storedFile.setSize(file.getSize());

			FileUploadReponse fileUploadReponse = new FileUploadReponse();
			fileUploadReponse.setFileProperties(Arrays.asList(storedFile));
			return Response.builder().code(200).message("Upload thành công").data(fileUploadReponse).build();
		} catch (IOException e) {
			e.printStackTrace();
			throw new BadRequestException("Upload thất bại");
		}
	}
}
