package xyz.ummo.user.data.govRoomData.model;

import java.util.ArrayList;

public interface ProductModel {
    String getProductId();
    String getProductName();
    String getProductDescription();
    String getProductProvider();
    ArrayList<String> getProductDocuments();
    String getProductCost();
    String getProductDuration();
    ArrayList<String> getProductSteps();
    Boolean getIsDelegated();
}
