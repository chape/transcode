package fs.transcode;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aliyuncs.mts.model.v20140618.SubmitJobsResponse;

import fs.App;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class AliyunMediaTranscodeServiceTest {

    @Autowired
    private AliyunMediaTranscodeService transcodeService;
    
    @Test
    public void testSubmitTranscodeJobString() {
        SubmitJobsResponse submitTranscodeJob = null;
        try{
            submitTranscodeJob = transcodeService.submitTranscodeJob("0000da436e5343d0a0df1767c1f98ac4.mp3");
        }catch(Exception e){
            Assert.fail("transcode exception");
        }
        Assert.assertTrue(null != submitTranscodeJob);
        
        if(null != submitTranscodeJob){
            System.out.println(transcodeService.getJobId(submitTranscodeJob));
            System.out.println(transcodeService.getState(submitTranscodeJob));
            System.out.println(transcodeService.isSuccess(submitTranscodeJob));
            
            System.out.println(transcodeService.getJobListByJobIds(Stream.of(transcodeService.getJobId(submitTranscodeJob)).collect(Collectors.toList())));
        }
    }

}
