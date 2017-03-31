package fs.transcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Setter
@NoArgsConstructor
@Component
public class TranscodeOutConfig {

    @Autowired
    private TranscodeConfigBean config;
    @NonNull private String outputObjectKey;
    private String templateId;//转码模版
    
    public TranscodeOutConfig(String outputObjectKey){
        this.outputObjectKey = outputObjectKey;
        this.templateId = config.getTranscodeTemplateId();
    }
    
    public TranscodeOutConfig(String outputObjectKey, String templateId){
        this.outputObjectKey = outputObjectKey;
        this.templateId = templateId;
    }
    
    public String toJSONString(){
        JSONObject jobConfig = new JSONObject();
        jobConfig.put("OutputObject", this.outputObjectKey);
        jobConfig.put("TemplateId", this.templateId);
        
        JSONArray jobConfigArray = new JSONArray();
        jobConfigArray.add(jobConfig);
        return jobConfigArray.toString();
    }
}
