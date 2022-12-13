package tech.farhand.mediventapps;

public class Model {
    String id, MedBatchNo, MedName, MedType, MedQty, MedPrice, MedExpDate, MedDesc;

    public Model(){

    }
    public Model(String id, String medBatchNo, String medName, String medType, String medQty, String medPrice, String medExpDate, String medDesc) {
        this.id = id;
        MedBatchNo = medBatchNo;
        MedName = medName;
        MedType = medType;
        MedQty = medQty;
        MedPrice = medPrice;
        MedExpDate = medExpDate;
        MedDesc = medDesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedBatchNo() {
        return MedBatchNo;
    }

    public void setMedBatchNo(String medBatchNo) {
        MedBatchNo = medBatchNo;
    }

    public String getMedName() {
        return MedName;
    }

    public void setMedName(String medName) {
        MedName = medName;
    }

    public String getMedType() {
        return MedType;
    }

    public void setMedType(String medType) {
        MedType = medType;
    }

    public String getMedQty() {
        return MedQty;
    }

    public void setMedQty(String medQty) {
        MedQty = medQty;
    }

    public String getMedPrice() {
        return MedPrice;
    }

    public void setMedPrice(String medPrice) {
        MedPrice = medPrice;
    }

    public String getMedExpDate() {
        return MedExpDate;
    }

    public void setMedExpDate(String medExpDate) {
        MedExpDate = medExpDate;
    }

    public String getMedDesc() {
        return MedDesc;
    }

    public void setMedDesc(String medDesc) {
        MedDesc = medDesc;
    }
}
