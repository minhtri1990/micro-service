package com.viettel.parking.camera.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.viettel.parking.camera.data.model.Camera;
import com.viettel.parking.camera.repository.CameraRepository;

@Controller
@RequestMapping("/live")
public class CameraLiveController {

	@Autowired
	private CameraRepository cameraRepository;

	@RequestMapping(value = "/camera/{deviceIdCamera}", method = RequestMethod.GET)
	public String init(@PathVariable("deviceIdCamera") String deviceIdCamera, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Camera camera = cameraRepository.findByDeviceIdCamera(deviceIdCamera);
		// Set to model
		if (camera != null) {
			String resource = camera.getWss(Camera.TYPE_HD);
			model.addAttribute("resource", resource);
		}

		model.addAttribute("camera", camera);
		return "VMSPlayer-live";
	}

}
