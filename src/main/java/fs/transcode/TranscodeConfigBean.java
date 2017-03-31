package fs.transcode;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * 转码所需配置信息(application.yml)
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix="aliyun.transcode")
public class TranscodeConfigBean {

    private String location; //Bucket 所在数据中心
    private String regionId; //转码服务所在区域
    private String bucketnameVoice;//未转码语音的oss桶名称
    private String bucketnameVoiceTrance;//已转码语音的oss桶名称
    private String pipelineId;//管道ID 绑定消息主题
    private String transcodeTemplateId;// 转码模板ID
    private String accessKeyId;//oss app key
    private String accessKeySecret;//oss secret key
    
}
