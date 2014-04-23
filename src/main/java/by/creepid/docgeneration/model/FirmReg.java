package by.creepid.docgeneration.model;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import by.creepid.docgeneration.fields.FieldsHelper;
import by.creepid.docgeneration.validation.GtinCheck;
import by.creepid.docsreporter.context.annotations.Image;
import by.creepid.docsreporter.context.annotations.TextStyling;
import fr.opensagres.xdocreport.core.document.SyntaxKind;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "reg")
@RequestScoped
public class FirmReg implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Image(bookmarks = {"offline"}, width = 40)
    private byte[] offlineChoice;
    @Image(bookmarks = {"web"}, width = 40)
    private byte[] webChoice;

    private ConnectionType type_connection;

    @GtinCheck
    private String gln;
    @NotEmpty
    @Size(max = 255)
    private String companyname;
    @Size(max = 255)
    private String lawtype;
    @NotEmpty
    @Size(max = 255)
    private String structural_affiliation;
    @NotEmpty
    @Size(max = 255)
    private String headorg;
    @NotEmpty
    @Size(max = 255)
    private String headorg_whereby_act;
    private String employees_number;

    @Image(bookmarks = {"logo"}, width = 200)
    private byte[] logo_file;
    @NotEmpty
    @Size(max = 2000)
    private String description_activity;
    @Size(max = 255)
    @Pattern(regexp = "(^$)|(^((https?|ftp)://|(www|ftp|)\\.|(^))[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$)")
    @TextStyling(syntaxKind = SyntaxKind.Html, syntaxWithDirective = true)
    private String web_address;
    @Size(max = 255)
    private String postcode;
    @NotNull
    @Min(0)
    private Long region;
    private String regionStr;
    @Size(max = 255)
    private String locality;
    @Min(0)
    private Long street_type;
    private String street_typeStr;
    @Size(max = 255)
    private String street;
    private String house_number;
    private String corps_number;
    private String office_number;
    @NotEmpty
    @Size(max = 255)
    private String service_bank;
    @NotEmpty
    @Size(max = 255)
    private String service_bank_address;
    @NotNull
    private String current_account;
    @NotNull
    private String bank_code;
    @NotNull
    private String unp;

    @NotEmpty
    @Size(max = 255)
    private String contact_name;
    @NotEmpty
    @Size(max = 255)
    private String contact_position;
    @NotEmpty
    @Size(max = 255)
    private String contact_phone;
    @NotEmpty
    @Size(max = 255)
    @Pattern(regexp = "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})")
    @TextStyling(syntaxKind = SyntaxKind.Html, syntaxWithDirective = true)
    private String contact_email;

    public ConnectionType getType_connection() {
        return type_connection;
    }

    public void setType_connection(ConnectionType type_connection) {
        this.type_connection = type_connection;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getLawtype() {
        return lawtype;
    }

    public void setLawtype(String lawtype) {
        this.lawtype = lawtype;
    }

    public String getStructural_affiliation() {
        return structural_affiliation;
    }

    public void setStructural_affiliation(String structural_affiliation) {
        this.structural_affiliation = structural_affiliation;
    }

    public String getHeadorg() {
        return headorg;
    }

    public void setHeadorg(String headorg) {
        this.headorg = headorg;
    }

    public String getHeadorg_whereby_act() {
        return headorg_whereby_act;
    }

    public void setHeadorg_whereby_act(String headorg_whereby_act) {
        this.headorg_whereby_act = headorg_whereby_act;
    }

    public byte[] getLogo_file() {
        return logo_file;
    }

    public void setLogo_file(byte[] logo_file) {
        this.logo_file = logo_file;
    }

    public String getDescription_activity() {
        return description_activity;
    }

    public void setDescription_activity(String description_activity) {
        this.description_activity = description_activity;
    }

    public String getWeb_address() {
        return web_address;
    }

    public void setWeb_address(String web_address) {
        this.web_address = web_address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getService_bank_address() {
        return service_bank_address;
    }

    public void setService_bank_address(String service_bank_address) {
        this.service_bank_address = service_bank_address;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_position() {
        return contact_position;
    }

    public void setContact_position(String contact_position) {
        this.contact_position = contact_position;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getService_bank() {
        return service_bank;
    }

    public void setService_bank(String service_bank) {
        this.service_bank = service_bank;
    }

    public String toString() {
        return FieldsHelper.getFieldsDump(this);
    }

    public Long getRegion() {
        return region;
    }

    public void setRegion(Long region) {
        this.region = region;
    }

    @JsonIgnore
    public String getRegionStr() {
        return regionStr;
    }

    public void setRegionStr(String regionStr) {
        this.regionStr = regionStr;
    }

    public Long getStreet_type() {
        return street_type;
    }

    public void setStreet_type(Long street_type) {
        this.street_type = street_type;
    }

    @JsonIgnore
    public String getStreet_typeStr() {
        return street_typeStr;
    }

    public void setStreet_typeStr(String street_typeStr) {
        this.street_typeStr = street_typeStr;
    }

    public String getGln() {
        return gln;
    }

    public void setGln(String gln) {
        this.gln = gln;
    }

    public String getEmployees_number() {
        return employees_number;
    }

    public void setEmployees_number(String employees_number) {
        this.employees_number = employees_number;
    }

    public String getHouse_number() {
        return house_number;
    }

    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }

    public String getCorps_number() {
        return corps_number;
    }

    public void setCorps_number(String corps_number) {
        this.corps_number = corps_number;
    }

    public String getOffice_number() {
        return office_number;
    }

    public void setOffice_number(String office_number) {
        this.office_number = office_number;
    }

    public String getCurrent_account() {
        return current_account;
    }

    public void setCurrent_account(String current_account) {
        this.current_account = current_account;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getUnp() {
        return unp;
    }

    public void setUnp(String unp) {
        this.unp = unp;
    }

    @JsonIgnore
    public byte[] getWebChoice() {
        return webChoice;
    }

    public void setWebChoice(byte[] webChoice) {
        this.webChoice = webChoice;
    }

    @JsonIgnore
    public byte[] getOfflineChoice() {
        return offlineChoice;
    }

    public void setOfflineChoice(byte[] offlineChoice) {
        this.offlineChoice = offlineChoice;
    }
}
