package xyz.ummo.user.data.govRoomData.model;

import java.util.ArrayList;

public interface DelegatedServiceModel {
//    String getServiceName();
//    String getId();
    String getDelegationId();
    String getDelegatedProductId();
    ArrayList getServiceProgress();
    String getServiceAgentId();
    String getServiceDate();
}
