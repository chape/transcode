package fs.transcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.json.JSONObject;

@Setter
@Getter
@NoArgsConstructor
@Component
public class OSSFile {
    
    private static TranscodeConfigBean config;
    
    private String location;
    private String bucket;
    private String objectKey;
    
    @Autowired
    public void setConfig(TranscodeConfigBean config) {
        OSSFile.config = config;
    }
    
    public OSSFile(String location, String bucket, String objectKey){
        this.location = location;
        this.bucket = bucket;
        this.objectKey = objectKey;
    }
    
    public OSSFile(String objectKey){
        this.location = OSSFile.config.getLocation();
        this.bucket = OSSFile.config.getBucketnameVoice();
        this.objectKey = objectKey;
    }
    
    public String toJsonString(){
        return toJson().toString();
    }

    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Bucket", this.bucket);
        jsonObject.put("Location", this.location);
        jsonObject.put("Object", this.objectKey);

        return jsonObject;
    }
}
