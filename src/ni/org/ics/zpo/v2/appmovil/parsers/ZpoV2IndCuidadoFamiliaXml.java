package ni.org.ics.zpo.v2.appmovil.parsers;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.util.Date;

/**
 * @author ics
 */
public class ZpoV2IndCuidadoFamiliaXml {

    @Element(required=false)
    private Date fechaHoyFci;
    @Element(required=false)
    private String cuantosLibrosFci;
    @Element(required=false)
    private String cuantasRevistasFci;
    @Element(required=false)
    private String materialesJugarFci;
    @Element(required=false)
    private String variedadJugarFci;
    @Element(required=false)
    private String actividadesJugarFci;
    @Element(required=false)
    private String encuestadorFci;


    @Element(required=false)
    private String group1;

    @Attribute
    private String id;
    @Element(required=false)
    private Meta meta;
    @Element(required=false)
    private String start;
    @Element(required=false)
    private String end;
    @Element(required=false)
    private String deviceid;
    @Element(required=false)
    private String simserial;
    @Element(required=false)
    private String phonenumber;
    @Element(required=false)
    private String imei;
    @Element(required=false)
    private Date today;
    @Attribute(required = false)
    private String version;


    public Date getFechaHoyFci() {
        return fechaHoyFci;
    }

    public void setFechaHoyFci(Date fechaHoyFci) {
        this.fechaHoyFci = fechaHoyFci;
    }

    public String getCuantosLibrosFci() {
        return cuantosLibrosFci;
    }

    public void setCuantosLibrosFci(String cuantosLibrosFci) {
        this.cuantosLibrosFci = cuantosLibrosFci;
    }

    public String getMaterialesJugarFci() {
        return materialesJugarFci;
    }

    public void setMaterialesJugarFci(String materialesJugarFci) {
        this.materialesJugarFci = materialesJugarFci;
    }

    public String getVariedadJugarFci() {
        return variedadJugarFci;
    }

    public void setVariedadJugarFci(String variedadJugarFci) {
        this.variedadJugarFci = variedadJugarFci;
    }

    public String getCuantasRevistasFci() {
        return cuantasRevistasFci;
    }

    public void setCuantasRevistasFci(String cuantasRevistasFci) {
        this.cuantasRevistasFci = cuantasRevistasFci;
    }

    public String getActividadesJugarFci() {
        return actividadesJugarFci;
    }

    public void setActividadesJugarFci(String actividadesJugarFci) {
        this.actividadesJugarFci = actividadesJugarFci;
    }

    public String getEncuestadorFci() {
        return encuestadorFci;
    }

    public void setEncuestadorFci(String encuestadorFci) {
        this.encuestadorFci = encuestadorFci;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getSimserial() {
        return simserial;
    }

    public void setSimserial(String simserial) {
        this.simserial = simserial;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
