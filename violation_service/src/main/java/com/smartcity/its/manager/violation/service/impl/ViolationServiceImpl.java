package com.smartcity.its.manager.violation.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.its.module.model.dto.PaginatedList;
import com.its.module.model.entity.ViolationTypeEntity;
import com.its.module.model.exception.BadRequestException;
import com.its.module.model.exception.ForbiddenException;
import com.its.module.model.exception.NotfoundException;
import com.its.module.model.response.BaseResponse;
import com.its.module.model.response.Meta;
import com.its.module.model.response.Response;
import com.its.module.utils.GsonUtils;
import com.its.module.utils.MathUtils;
import com.its.module.utils.ObjectUtils;
import com.smartcity.its.manager.violation.configuration.WebConfig;
import com.smartcity.its.manager.violation.model.dto.ViolationDto;
import com.smartcity.its.manager.violation.model.dto.ViolationStatusDto;
import com.smartcity.its.manager.violation.model.dto.ViolationTypeDto;
import com.smartcity.its.manager.violation.model.entity.ViolationEntity;
import com.smartcity.its.manager.violation.model.entity.ViolationStatusEntity;
import com.smartcity.its.manager.violation.model.entity.WarningConfigEntity;
import com.smartcity.its.manager.violation.model.entity.WarningConfigItemEntity;
import com.smartcity.its.manager.violation.model.entity.WarningLogEntity;
import com.smartcity.its.manager.violation.model.request.ConfirmViolationRequest;
import com.smartcity.its.manager.violation.model.request.SmsRequest;
import com.smartcity.its.manager.violation.model.request.scp.ScpViolation;
import com.smartcity.its.manager.violation.repository.DeviceRepository;
import com.smartcity.its.manager.violation.repository.EmailFeignClient;
import com.smartcity.its.manager.violation.repository.IocFeignClient;
import com.smartcity.its.manager.violation.repository.ViolationRepository;
import com.smartcity.its.manager.violation.repository.ViolationStatusRepository;
import com.smartcity.its.manager.violation.repository.ViolationTypeRepository;
import com.smartcity.its.manager.violation.repository.WarningConfigItemRepository;
import com.smartcity.its.manager.violation.repository.WarningConfigRepository;
import com.smartcity.its.manager.violation.repository.WarningConfirmRepository;
import com.smartcity.its.manager.violation.repository.WarningLogRepository;
import com.smartcity.its.manager.violation.service.ViolationService;
import com.smartcity.its.manager.violation.utils.ExtractUtils;
import com.smartcity.its.manager.violation.utils.ViolationUtils;
import com.smartcity.its.manager.violation.utils.ViolationUtils.SendingMethod;
import com.smartcity.its.manager.violation.utils.ViolationUtils.SendingType;
import com.smartcity.its.manager.violation.utils.ViolationUtils.Type;

@Service
public class ViolationServiceImpl implements ViolationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ViolationServiceImpl.class);

	@Autowired
	private IocFeignClient iocFeignClient;

	@Autowired
	private DeviceRepository deviceRepository;

	@Autowired
	private ViolationTypeRepository violationTypeRepository;

	@Autowired
	private WarningConfigRepository warningConfigRepository;

	@Autowired
	private WarningConfigItemRepository warningConfigItemRepository;

	@Autowired
	private WarningLogRepository warningLogRepository;

	@Autowired
	private ViolationStatusRepository violationStatusRepository;

	@Autowired
	private EmailFeignClient emailFeignClient;

	@Autowired
	private ViolationRepository violationRepository;

	@Override
	public Response<?> getOne(String id, Authentication authentication) {
		ViolationEntity violation = violationRepository.findByIdAndIsActiveIsTrue(id);
		if(violation == null)
			throw new NotfoundException("Không tìm thấy vi phạm");
		if(deviceRepository.isPermitted(violation.getDeviceId(), authentication))
			throw new ForbiddenException();
		return Response.builder().code(HttpStatus.OK.value()).data(convertDto(Arrays.asList(violation)).get(0)).build();
	}

	@Override
	public Response<?> search(Map<String, String> params, Integer page, Integer pageSize,
			Authentication authentication) {
		PaginatedList<ViolationEntity> paginated = searchViolationEntities(params, page, pageSize, authentication);
		int totalEl = paginated.getTotal();
		Meta meta = Meta.builder().page(page).pageSize(pageSize).totalElements(totalEl)
				.totalPages(MathUtils.calcNumPages(totalEl, pageSize)).build();
		return Response.builder().code(200).meta(meta).data(convertDto(paginated.getEntities())).build();
	}

	private final String FILE_TYPE = "file_type";

	@Override
	public ResponseEntity extract(Map<String, String> params, Integer page, Integer pageSize,
			Authentication authentication) {
		List<ViolationEntity> violations = searchViolationEntities(params, page, pageSize, authentication).getEntities();

		if (!params.containsKey(FILE_TYPE))
			params.put(FILE_TYPE, "excel");
		try {
			ByteArrayResource resource;
			String contentType;
			switch (params.get(FILE_TYPE)) {
			case "pdf":
				resource = new ByteArrayResource(ExtractUtils.violationPdf(convertDto(violations)).toByteArray());
				contentType = "application/pdf";
				break;
			default:
				resource = new ByteArrayResource(ExtractUtils.violationExcel(convertDto(violations)).toByteArray());
				contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			}
			return ResponseEntity.ok().contentLength(resource.contentLength())
					.contentType(MediaType.parseMediaType(contentType)).body(resource);
		} catch (Exception e) {
			LOGGER.error("extract excel ", e);
			throw new BadRequestException("Không thể trích xuất file");
		}
	}


	private PaginatedList<ViolationEntity> searchViolationEntities(Map<String, String> params, Integer page, Integer pageSize,
			Authentication authentication) {
		Integer offset = (page - 1) * pageSize;
		String licensePlate = params.get("licensePlate");
		String deviceId = params.get("deviceId");
		Integer status = params.containsKey("status") ? Integer.valueOf(params.get("status")) : null;
		Long startTime = params.containsKey("startTime") ? Long.parseLong(params.get("startTime")) : null;
		Long endTime = params.containsKey("endTime") ? Long.parseLong(params.get("endTime")) : null;

		String deviceIds = null;
		if (deviceId == null)
			deviceIds = String.join(",", deviceRepository.getCameraIds(authentication));
		else if (deviceRepository.isPermitted(deviceId, authentication))
			deviceIds = deviceId.trim();
		
		System.out.println("device ids: " + deviceIds);
		if(licensePlate == null) licensePlate = "%";
		else licensePlate = licensePlate.trim().replace("%", "\\%").replace("_", "\\_");
		licensePlate = "%"+licensePlate+"%";
		
		List<ViolationEntity> violations = violationRepository.searchViolations(deviceIds, licensePlate, status, startTime, endTime, pageSize,
				offset);
		Integer total = violationRepository.countSearchViolations(deviceIds, licensePlate, status, startTime, endTime);
		return new PaginatedList(violations, total);
	}

	@Override
	public Response<?> confirmNotViolated(String violationId, ConfirmViolationRequest confirmViolationRequest,
			Authentication authentication) {
		ViolationEntity violation = violationRepository.findByIdAndIsActiveIsTrue(violationId);
		if (violation == null)
			throw new NotfoundException(WebConfig.translate("not_found"));
		violation.setIsViolation(confirmViolationRequest.getIsViolation());
		violation.setDescription(confirmViolationRequest.getDescription().trim());
		violationRepository.save(violation);
		return Response.builder().code(200).message(WebConfig.translate("process_success")).build();
	}

	/**
	 * Nhận dữ liệu từ IVA agent
	 */
	@Override
	public Response receiveViolationFromScp(List<ScpViolation> entities) {
		Map<String, ViolationTypeEntity> mapViolationType = new HashMap();
		violationTypeRepository.findAll().stream().forEach(violationType -> {
			mapViolationType.put(violationType.getCode(), violationType);
		});

		entities.stream().forEach(entity -> {
			ViolationEntity violation = ViolationEntity.builder().id(entity.getId())
					.type(mapViolationType.get(entity.getType()).getId()).vehicleType(entity.getVehicleType())
					.licensePlate(entity.getLicensePlate()).description(entity.getDescription())
					.address(entity.getAddress()).timestamp(entity.getTimestamp()).status(0)
					.deviceId(entity.getDeviceId()).isViolation(true).location(entity.getLocation()).build();
			ObjectUtils.setCreateHistory(violation, 0);
			violation = violationRepository.save(violation);
			for (Integer supervisorId : warningConfigRepository.getAllSupervisorIds()) {
	            sendWarningToSupervisor(violation, mapViolationType.get(entity.getType()), supervisorId);
	        }
		});
		return Response.builder().code(200).message("ok").build();
	}

	@Override
	public BaseResponse sendDaily() {
//		LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
//		if (yesterday.getMinute() > 30)
//			yesterday = yesterday.plusHours(1);
//		LocalDateTime offsetTime = LocalDateTime.of(yesterday.toLocalDate(), LocalTime.of(yesterday.getHour(), 0));
//		Long startTime = Timestamp.valueOf(offsetTime).getTime();
//		Long endTime = Timestamp.valueOf(offsetTime.plusDays(1)).getTime();
////        Set<Integer> userIds = deviceRepository.getAllUserIds();
////        for(ViolationTypeEntity violationType:violationTypeRepository.findByIsActiveIsTrue()) {
////            for(Integer userId:userIds) {
////                int numViolations = countViolationByTypeAndTime(userId, false, violationType.getCode(), startTime, endTime);
////                sendWarningToSupervisor(numViolations, violationType, userId, startTime, endTime);
////            }
////        }
//		return Response.builder().code(200).message(WebConfig.translate("process_success")).build();
		return null;
	}



	private List<ViolationDto> convertDto(List<ViolationEntity> violations) {
		Map<Integer, ViolationTypeDto> mapType = new HashMap<>();
		for (ViolationTypeEntity violationType : violationTypeRepository.findByIsActiveIsTrue())
			mapType.put(violationType.getId(), GsonUtils.mapObject(violationType, ViolationTypeDto.class));

		Map<Integer, ViolationStatusDto> mapStatus = new HashMap<>();
		for (ViolationStatusEntity violationStatus : violationStatusRepository.findAll())
			mapStatus.put(violationStatus.getId(), GsonUtils.mapObject(violationStatus, ViolationStatusDto.class));

		List<ViolationDto> violationDtos = new ArrayList<>();
		for (ViolationEntity violation : violations) {
			ViolationDto violationDto = GsonUtils.mapObject(violation, ViolationDto.class);
			violationDto.setViolationType(mapType.get(violation.getType()));
			violationDto.setStatusDto(mapStatus.get(violation.getStatus()));
			violationDtos.add(violationDto);
		}

		return violationDtos;
	}

	public void sendWarningToSupervisor(Integer numViolations, ViolationTypeEntity violationType, Integer userId,
			Long startTime, Long endTime) {
		WarningConfigItemEntity warningConfigItem = warningConfigItemRepository
				.findByUserIdAndViolationTypeIdAndIsActiveIsTrue(userId, Type.VIOLATION.ordinal(),
						violationType.getId());
		if (warningConfigItem == null)
			return;
		WarningConfigEntity warningConfig = warningConfigRepository.getByUserIdAndTypeNative(userId,
				Type.VIOLATION.ordinal());
		if (warningConfig == null)
			return;

		List<String> emails = new ArrayList<>();
		if (warningConfig.getEmails() != null && !"".equals(warningConfig.getEmails()))
			emails = Arrays.asList(warningConfig.getEmails().split(";"));
		List<String> phones = new ArrayList<>();
		if (warningConfig.getPhones() != null && !"".equals(warningConfig.getPhones()))
			phones = Arrays.asList(warningConfig.getPhones().split(";"));

		/**
		 * if we need to send warning daily
		 */

		if (warningConfigItem.getSendingType() == SendingType.DAILY.ordinal()) {
			List<Integer> sendingMethods = ViolationUtils.convertStringToList(warningConfigItem.getSendingMethod());

			if (sendingMethods.contains(SendingMethod.EMAIL.ordinal()) && emails.size() != 0)
				sendWarningEmailAboutViolation(emails, numViolations, violationType, userId, startTime, endTime);
			if (sendingMethods.contains(SendingMethod.SMS.ordinal()) && phones.size() != 0)
				sendWarningSmsAboutViolation(phones, numViolations, violationType, userId, startTime, endTime);
		}
	}

	public int sendWarningEmailAboutViolation(List<String> emails, Integer numViolations,
			ViolationTypeEntity violationType, Integer userId, Long startTime, Long endTime) {
		String title = "Email cảnh báo vi phạm trên cao tốc [Định kỳ hằng ngày]";
		String content = String.format("Đã ghi nhận được %s trường vi phạm %s trong thời gian từ %s đến %s ",
				numViolations, violationType.getName(), convertTimestamp(startTime), convertTimestamp(endTime));
		int cnt = 0;
		for (String email : emails) {
			cnt++;
			LOGGER.info("Send email to " + email + " || title: " + title + " || content: " + content);
			try {
				// only send email in server dev
				sendEmail(email, title, content);
				WarningLogEntity warningLog = WarningLogEntity.builder().userId(userId)
						.sendingType(SendingType.DAILY.ordinal()).sendingMethod(SendingMethod.EMAIL.ordinal())
						.sendingDestination(email).title(title).content(content).createdDate(LocalDateTime.now())
						.build();
				warningLogRepository.save(warningLog);
			} catch (Exception e) {
				LOGGER.error("cannot send email to " + email);
			}
		}
		return cnt;
	}

	public int sendWarningSmsAboutViolation(List<String> phones, Integer numViolations,
			ViolationTypeEntity violationType, Integer userId, Long startTime, Long endTime) {
		int cnt = 0;
		String content = "[ITS] Thong bao dinh ky: " + String.format("%s -- So luong: %s -- Tu %s den %s",
				violationType.getEnglishName(), numViolations, convertTimestamp(startTime), convertTimestamp(endTime));

		for (String phone : phones) {
			cnt++;
			LOGGER.info("Send sms to " + phone + " || content: " + content);
			try {
				// only send sms in server dev
				sendSms(phone, content);
				WarningLogEntity warningLog = WarningLogEntity.builder().userId(userId)
						.sendingType(SendingType.IMMEDIATE.ordinal()).sendingMethod(SendingMethod.SMS.ordinal())
						.sendingDestination(phone).title("SMS").content(content).createdDate(LocalDateTime.now())
						.build();
				warningLogRepository.save(warningLog);
			} catch (Exception e) {
				LOGGER.error("cannot send sms to " + phone, e);
			}
		}
		return cnt;
	}

	// TODO set is_urgent and default item
	// TODO use thread pool
	@Value("${SERVER_MODE}")
	private String SERVER_MODE;

	public void sendWarningToSupervisor(ViolationEntity violation, ViolationTypeEntity violationType, Integer userId) {
		WarningConfigItemEntity warningConfigItem = warningConfigItemRepository
				.findByUserIdAndViolationTypeIdAndIsActiveIsTrue(userId, Type.VIOLATION.ordinal(),
						violationType.getId());
		if (warningConfigItem == null)
			return;
		WarningConfigEntity warningConfig = warningConfigRepository.getByUserIdAndTypeNative(userId,
				Type.VIOLATION.ordinal());
		if (warningConfig == null)
			return;

		List<String> emails = new ArrayList<>();
		if (warningConfig.getEmails() != null && !"".equals(warningConfig.getEmails()))
			emails = Arrays.asList(warningConfig.getEmails().split(";"));
		List<String> phones = new ArrayList<>();
		if (warningConfig.getPhones() != null && !"".equals(warningConfig.getPhones()))
			phones = Arrays.asList(warningConfig.getPhones().split(";"));

		/**
		 * if we need to send warning immediately
		 */

		if (warningConfigItem.getSendingType() == SendingType.IMMEDIATE.ordinal()) {
			List<Integer> sendingMethods = ViolationUtils.convertStringToList(warningConfigItem.getSendingMethod());
			/**
			 * send by email
			 */
			Timer timer = new Timer();
			SendEmailTimeTask sendEmailTimeTask = new SendEmailTimeTask(timer, violation, violationType, userId, emails,
					phones, sendingMethods);
			if (warningConfigItem.getRepetition() == null || warningConfigItem.getRepetition() == 0)
				timer.schedule(sendEmailTimeTask, 0);
			else
				timer.schedule(sendEmailTimeTask, 0, warningConfigItem.getRepetition() * 60 * 1000);
		}
	}

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");

	private String convertTimestamp(Long timestamp) {
		return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("Asia/Ho_Chi_Minh"))
				.format(formatter);
	}

	public int sendWarningEmailAboutViolation(List<String> emails, ViolationEntity violation,
			ViolationTypeEntity violationType, Integer userId, boolean isFirst) {
		String title = "Email cảnh báo vi phạm trên cao tốc";
		String content = String.format("ID: %s -- User ID: %s -- Loại vi phạm: %s -- Biển số xe: %s -- Thời gian: %s",
				violation.getId(), userId, violationType.getName(), violation.getLicensePlate(),
				convertTimestamp(violation.getTimestamp().longValue()));
		int cnt = 0;
		for (String email : emails) {
			cnt++;
			LOGGER.info("Send email to " + email + " || title: " + title + " || content: " + content);
			try {
				sendEmail(email, title, content);
				WarningLogEntity warningLog = WarningLogEntity.builder().violationId(violation.getId()).userId(userId)
						.sendingType(SendingType.IMMEDIATE.ordinal()).sendingMethod(SendingMethod.EMAIL.ordinal())
						.sendingDestination(email).title(title).content(content).createdDate(LocalDateTime.now())
						.build();
				warningLogRepository.save(warningLog);
			} catch (Exception e) {
				LOGGER.error("cannot send email to " + email);
			}
		}
		return cnt;
	}

	public int sendWarningSmsAboutViolation(List<String> phones, ViolationEntity violation,
			ViolationTypeEntity violationType, Integer userId, boolean isFirst) {
		int cnt = 0;
		String content = "[ITS] Thong bao vi pham: "
				+ String.format("%s -- Bien so: %s -- Thoi gian: %s", violationType.getEnglishName(),
						violation.getLicensePlate(), convertTimestamp(violation.getTimestamp().longValue()));

		for (String phone : phones) {
			cnt++;
			LOGGER.info("Send sms to " + phone + " || content: " + content);
			try {
				sendSms(phone, content);
				WarningLogEntity warningLog = WarningLogEntity.builder().violationId(violation.getId()).userId(userId)
						.sendingType(SendingType.IMMEDIATE.ordinal()).sendingMethod(SendingMethod.SMS.ordinal())
						.sendingDestination(phone).title("SMS").content(content).createdDate(LocalDateTime.now())
						.build();
				warningLogRepository.save(warningLog);
			} catch (Exception e) {
				LOGGER.error("cannot send sms to " + phone, e);
			}
		}
		return cnt;
	}

	@Value("${FROM_EMAIL_ADDRESS}")
	private String FROM_EMAIL;
	private final ExecutorService executorService = Executors.newFixedThreadPool(2);

	private void sendEmail(String toEmail, String subject, String body) {
		if ("dev".equals(SERVER_MODE))
			executorService.submit(() -> {
				Response<String> resp = emailFeignClient.sendEmail(subject, FROM_EMAIL, toEmail, body);
				LOGGER.info("email response", resp.getCode() + " " + resp.getData());
			});
	}

	private void sendSms(String phoneNumber, String body) {
		if ("dev".equals(SERVER_MODE)) {
			SmsRequest smsRequest = SmsRequest.builder().phoneNumber(phoneNumber).message(body).sourceType(0).build();
			executorService.submit(() -> {
				Response<String> resp = emailFeignClient.sendSms(smsRequest);
				LOGGER.info("sms response", resp.getCode() + " " + resp.getData());
			});
		}
	}

	@Autowired
	private WarningConfirmRepository warningConfirmRepository;

	@Value("${MAX_TIMES}")
	private Integer MAX_TIMES = 5;

	class SendEmailTimeTask extends TimerTask {
		private ViolationEntity violation;
		private ViolationTypeEntity violationType;
		private Integer userId;
		private Timer timer;
		private List<String> emails;
		private List<String> phones;
		private boolean isFirst;
		private List<Integer> sendingMethods;
		private Integer sendTimes;

		public SendEmailTimeTask(Timer timer, ViolationEntity violation, ViolationTypeEntity violationType, Integer userId,
				List<String> emails, List<String> phones, List<Integer> sendingMethods) {
			this.violation = violation;
			this.violationType = violationType;
			this.userId = userId;
			this.timer = timer;
			this.emails = emails;
			this.phones = phones;
			this.isFirst = true;
			this.sendingMethods = sendingMethods;
			this.sendTimes = 0;
		}

		private void stopTimer() {
			timer.cancel();
			timer.purge();
			timer = null;
		}

		@Override
		public void run() {
			int cnt = 0;
			/**
			 * stop after confirm
			 */
			LocalDateTime lastConfirm = warningConfirmRepository.getLastByDeviceId(violation.getDeviceId());
			// TODO
			// LocalDateTime createdTime = LocalDateTime.parse(violation.getCreatedTime(),
			// DateTimeFormatter.ISO_DATE_TIME);
			LOGGER.info(String.format("Last confirm: %s, created date: %s", lastConfirm, violation.getTimestamp()));
			if (lastConfirm != null && Timestamp.valueOf(lastConfirm).getTime() >= violation.getTimestamp()) {
				LOGGER.info(String.format("Stop timer by user %s about violation %s", userId, violation.getId()));
				stopTimer();
				return;
			}

			if (sendTimes >= MAX_TIMES) {
				LOGGER.info(String.format("Stop timer because limited times %s about violation %s", userId,
						violation.getId()));
				stopTimer();
				return;
			}

			LOGGER.info(String.format("Start send %s warning user %s about %s", sendTimes, userId, violation.getId()));
			if (sendingMethods.contains(SendingMethod.EMAIL.ordinal()) && emails.size() != 0)
				cnt += sendWarningEmailAboutViolation(emails, violation, violationType, userId, isFirst);

			if (sendingMethods.contains(SendingMethod.SMS.ordinal()) && phones.size() != 0)
				cnt += sendWarningSmsAboutViolation(phones, violation, violationType, userId, isFirst);

			if (cnt == 0) {
				LOGGER.info(String.format("Stop because have no email or phone to send (userId: %s, violationId: %s)",
						userId, violation.getId()));
				stopTimer();
				return;
			}
			isFirst = false;
			sendTimes++;
		}
	}
}
