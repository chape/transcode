package fs.transcode;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.mts.model.v20140618.QueryJobListRequest;
import com.aliyuncs.mts.model.v20140618.QueryJobListResponse;
import com.aliyuncs.mts.model.v20140618.SubmitJobsRequest;
import com.aliyuncs.mts.model.v20140618.SubmitJobsResponse;
import com.aliyuncs.mts.model.v20140618.SubmitJobsResponse.JobResult;
import com.aliyuncs.mts.model.v20140618.SubmitJobsResponse.JobResult.Job;
import com.aliyuncs.profile.DefaultProfile;

@Component
public class AliyunMediaTranscodeService {

    private static TranscodeConfigBean config;
    private static DefaultAcsClient client;
    private static Logger LOGGER = LoggerFactory.getLogger(AliyunMediaTranscodeService.class);
    
    //静态注入
    @Autowired
    public void setConfig(TranscodeConfigBean config) {
        AliyunMediaTranscodeService.config = config;
        if(null == AliyunMediaTranscodeService.config){
            LOGGER.error("aliyun transcode configure bean init error, the application will exit!");
            System.exit(-1);
        }
        DefaultProfile profile = DefaultProfile.getProfile(config.getRegionId(), config.getAccessKeyId(), config.getAccessKeySecret());
        client = new DefaultAcsClient(profile);
    }

    public DefaultAcsClient getDefaultAcsClient(){
        return client;
    }
    
    public SubmitJobsResponse submitTranscodeJob(String inputObjectKey) {
        if(StringUtils.isEmpty(inputObjectKey)){
            return null;
        }
        OSSFile inputFile = new OSSFile(inputObjectKey);
        SubmitJobsResponse response = submitTranscodeJob(inputFile);
        return response;
    }
    
    public SubmitJobsResponse submitTranscodeJob(String inputObjectKey, String outputObjectKey) {
        if(StringUtils.isEmpty(inputObjectKey) || StringUtils.isEmpty(outputObjectKey)){
            return null;
        }
        OSSFile inputFile = new OSSFile(inputObjectKey);
        return submitTranscodeJob(inputFile,outputObjectKey);
    }

    public SubmitJobsResponse submitTranscodeJob(OSSFile inputFile) {
        return submitTranscodeJob(inputFile, inputFile.getObjectKey());
    }
    
    public SubmitJobsResponse submitTranscodeJob(OSSFile inputFile, String outputObjectKey) {
        if (null == inputFile) {
            return null;
        }
        SubmitJobsRequest request = new SubmitJobsRequest();
        SubmitJobsResponse response = null;

        request.setInput(inputFile.toJsonString());//// 作业输入，包括InputFile以及其它一些转码作业输入配置参数
        request.setOutputBucket(config.getBucketnameVoiceTrance());// 转码输出Bucket
        request.setPipelineId(config.getPipelineId());// 管道ID 绑定消息主题
        request.setOutputLocation(config.getLocation());// 输出 Bucket 所在数据中心

        TranscodeOutConfig outputConfig = new TranscodeOutConfig(outputObjectKey, config.getTranscodeTemplateId());
        request.setOutputs(outputConfig.toJSONString());// 作业输出配置由模板ID，水印列表，OutputFile，输出文件等属性构成。

        try {
            response = client.getAcsResponse(request);
        } catch (ServerException e) {
            throw new RuntimeException("Aliyun Media Server Transcode failed");
        } catch (ClientException e) {
            throw new RuntimeException("Aliyun Media Client Transcode failed");
        }
        return response;
    }
    
    public List<com.aliyuncs.mts.model.v20140618.QueryJobListResponse.Job> getJobListByJobIds(List<String> jobIds){
        QueryJobListRequest request = new QueryJobListRequest();
        request.setJobIds(String.join(",", jobIds));
        
        QueryJobListResponse response = null;
        try {
            response = client.getAcsResponse(request);
        } catch (ServerException e) {
            throw new RuntimeException("Aliyun Media Server Query failed");
        } catch (ClientException e) {
            throw new RuntimeException("Aliyun Media Client Query failed");
        }
        return response.getJobList();
    }
    
    public Map<String,String> getJobStateByJobIds(List<String> jobIds){
        List<com.aliyuncs.mts.model.v20140618.QueryJobListResponse.Job> queryJobs = getJobListByJobIds(jobIds);
        if(CollectionUtils.isEmpty(queryJobs)){
            return Collections.emptyMap();
        }
        return queryJobs.stream().collect(Collectors.toMap(k -> k.getJobId(), v -> v.getState(), (k,v) -> v));
    }
    
    public boolean isSuccess(SubmitJobsResponse response){
        if(null == response){
            return false;
        }
        List<JobResult> jobResultList = response.getJobResultList();
        if(null == jobResultList || jobResultList.isEmpty()){
            return false;
        }
        return jobResultList.get(0).getSuccess();
    }
    
    public String getJobId(SubmitJobsResponse response){
        if(null == response){
            return CommonConstant.EMPTY_STRING;
        }
        List<JobResult> jobResultList = response.getJobResultList();
        if(null == jobResultList || jobResultList.isEmpty()){
            return CommonConstant.EMPTY_STRING;
        }
        Job job = jobResultList.get(0).getJob();
        if(null == job){
            return CommonConstant.EMPTY_STRING;
        }
        return job.getJobId();
    }
    
    public String getState(SubmitJobsResponse response){
        if(null == response){
            return CommonConstant.EMPTY_STRING;
        }
        List<JobResult> jobResultList = response.getJobResultList();
        if(null == jobResultList || jobResultList.isEmpty()){
            return CommonConstant.EMPTY_STRING;
        }
        Job job = jobResultList.get(0).getJob();
        if(null == job){
            return CommonConstant.EMPTY_STRING;
        }
        return job.getState();
    }
}
