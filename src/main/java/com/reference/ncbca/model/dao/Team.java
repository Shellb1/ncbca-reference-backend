package com.reference.ncbca.model.dao;

public record Team(Integer teamId, String name, Integer conferenceId, String conferenceName, String coach, Boolean active) {

}
