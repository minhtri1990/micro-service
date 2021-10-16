package com.viettel.parking.camera.data.dto;

import java.util.ArrayList;
import java.util.List;

public class ObservationUpdate {

	private List<Observation> observations = new ArrayList<Observation>();

	public List<Observation> getObservations() {
		return observations;
	}

	public void setObservations(List<Observation> observations) {
		this.observations = observations;
	}

	public void addObservation(String value) {
		Observation observation = new Observation();
		observation.setValue(value);
		if (observations == null) {
			observations = new ArrayList<Observation>();
		}
		observations.add(observation);
	}
}
