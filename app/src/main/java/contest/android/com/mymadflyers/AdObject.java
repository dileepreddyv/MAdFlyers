package contest.android.com.mymadflyers;

public class AdObject {
    private String companyName;
    private String offerDesc;
    private String companyAddress;
    AdObject(String text1, String text2, String text3){
        companyName = text1;
        offerDesc = text2;
        companyAddress = text3;
    }

    AdObject(){

    }
    private String businessName;
    private String addr1;
    private String addr2;
    private String addr3;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String desc;
    private String username;
    private String fileFullPath;
    private String phone;
    /**
     * @return the businessName
     */
    public String getBusinessName() {
        return businessName;
    }
    /**
     * @param businessName the businessName to set
     */
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    /**
     * @return the addr1
     */
    public String getAddr1() {
        return addr1;
    }
    /**
     * @param addr1 the addr1 to set
     */
    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }
    /**
     * @return the addr2
     */
    public String getAddr2() {
        return addr2;
    }
    /**
     * @param addr2 the addr2 to set
     */
    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }
    /**
     * @return the addr3
     */
    public String getAddr3() {
        return addr3;
    }
    /**
     * @param addr3 the addr3 to set
     */
    public void setAddr3(String addr3) {
        this.addr3 = addr3;
    }
    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }
    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }
    /**
     * @return the state
     */
    public String getState() {
        return state;
    }
    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }
    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }
    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }
    /**
     * @return the pincode
     */
    public String getPincode() {
        return pincode;
    }
    /**
     * @param pincode the pincode to set
     */
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }
    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * @return the fileFullPath
     */
    public String getFileFullPath() {
        return fileFullPath;
    }
    /**
     * @param fileFullPath the fileFullPath to set
     */
    public void setFileFullPath(String fileFullPath) {
        this.fileFullPath = fileFullPath;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOfferDesc() {
        return offerDesc;
    }

    public void setOfferDesc(String offerDesc) {
        this.offerDesc = offerDesc;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }
}