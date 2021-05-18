package com.fourthservice.services;

import com.fourthservice.models.pojos.DaysLeftToCompleteTaskPojo;
import com.fourthservice.models.pojos.FullTaskResultPojo;
import com.fourthservice.models.pojos.JoinerPojo;
import com.fourthservice.models.pojos.TaskPojo;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.util.*;

@Service
public class ReportService {

    @Value("${third.service.endpoint}")
    private String thirdServiceEndpoint;

    @Value("${file.toWrite.reports.topJoiner}")
    private String topJoinerPathFile;

    @Value("${file.toWrite.reports.uncompletedTask}")
    private String uncompletedTaskPathFile;

    @Value("${file.toWrite.reports.completedTask}")
    private String completedTaskPathFile;

    @Value("${file.toWrite.reports.uncompletedBydefinedJoiner}")
    private String uncompletedBydefinedJoinerPathFile;

    @Value("${file.toWrite.reports.completedBydefinedJoiner}")
    private String completedBydefinedJoinerPathFile;

    @Value("${file.toWrite.reports.timeLeft}")
    private String timeLeftPathFile;

    public String createTopJoinersReportService(String numberOfJoiners, String stack){
        String[] headers = new String[8];
        List<String[]> stringList = new ArrayList<>();

        headers[0] = "JoinerId";
        headers[1] = "IdentificationNumber";
        headers[2] = "Name";
        headers[3] = "LastName";
        headers[4] = "EnglishLevel";
        headers[5] = "DomainExperience";
        headers[6] = "RoleName";
        headers[7] = "TasksCompleted";
        stringList.add(headers);


        String uri = thirdServiceEndpoint + "/top-joiners-by-stack?numberOfJoiners=%s&stack=%s";
        var restTemplate = new RestTemplate();
        uri = String.format(uri, numberOfJoiners, stack);

        ResponseEntity<List<JoinerPojo>> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<JoinerPojo>>() {}
        );

        List<JoinerPojo> joiners = responseEntity.getBody();

        joiners.forEach(joiner -> {
            String[] results = new String[8];
            results[0] = joiner.getId().toString();
            results[1] = joiner.getIdentificationNumber().toString();
            results[2] = joiner.getName();
            results[3] = joiner.getLastname();
            results[4] = joiner.getEnglishLevel();
            results[5] = joiner.getDomainExperience();
            results[6] = joiner.getRole().getName();
            results[7] = joiner.getTasksCompleted().toString();
            stringList.add(results);
        });
        try{
            CSVWriter writer = new CSVWriter(new FileWriter(topJoinerPathFile));
            writer.writeAll(stringList);
            writer.close();
        }catch (Exception exception){
            exception.printStackTrace();
            return "Unable to write file";
        }
        return "Successfully created the report";
    }

    public String createFullTaskReportService(){
        String[] headers = new String[8];
        List<String[]> completedList = new ArrayList<>();
        List<String[]> uncompletedList = new ArrayList<>();

        headers[0] = "TaskId";
        headers[1] = "Name";
        headers[2] = "Description";
        headers[3] = "EstimatedRequiredHours";
        headers[4] = "Stack";
        headers[5] = "ParentTask";
        headers[6] = "JoinerDatabaseId";
        headers[7] = "JoinerName";
        completedList.add(headers);
        uncompletedList.add(headers);

        String uri = thirdServiceEndpoint + "/task-report";
        var restTemplate = new RestTemplate();
        uri = String.format(uri);

        ResponseEntity<FullTaskResultPojo> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<FullTaskResultPojo>() {}
        );

        FullTaskResultPojo taskPojoMap = responseEntity.getBody();

        taskPojoMap.getFinished().forEach( task -> {
            String[] results = new String[8];
            results[0] = task.getId().toString();
            results[1] = task.getName();
            results[2] = task.getDescription();
            results[3] = task.getEstimatedRequiredHours().toString();
            results[4] = task.getStack();
            results[5] = task.getParentTask() == null?"":task.getParentTask().toString();
            results[6] = task.getJoinerInCharge().getId().toString();
            results[7] = task.getJoinerInCharge().getName();
            completedList.add(results);
        });
        try{
            CSVWriter writer = new CSVWriter(new FileWriter(completedTaskPathFile));
            writer.writeAll(completedList);
            writer.close();
        }catch (Exception exception){
            exception.printStackTrace();
            return "Unable to write file";
        }

        taskPojoMap.getUnfinished().forEach( task -> {
            String[] results = new String[8];
            results[0] = task.getId().toString();
            results[1] = task.getName();
            results[2] = task.getDescription();
            results[3] = task.getEstimatedRequiredHours().toString();
            results[4] = task.getStack();
            results[5] = task.getParentTask() == null?"":task.getParentTask().toString();
            results[6] = task.getJoinerInCharge().getId().toString();
            results[7] = task.getJoinerInCharge().getName();
            uncompletedList.add(results);
        });

        try{
            CSVWriter writer = new CSVWriter(new FileWriter(uncompletedTaskPathFile));
            writer.writeAll(uncompletedList);
            writer.close();
        }catch (Exception exception){
            exception.printStackTrace();
            return "Unable to write file";
        }


        return "Successfully created the report";
    }

    public String createJoinerTaskReportService(String joinerId){
        String[] headers = new String[8];
        List<String[]> completedList = new ArrayList<>();
        List<String[]> uncompletedList = new ArrayList<>();

        headers[0] = "TaskId";
        headers[1] = "Name";
        headers[2] = "Description";
        headers[3] = "EstimatedRequiredHours";
        headers[4] = "Stack";
        headers[5] = "ParentTask";
        headers[6] = "JoinerDatabaseId";
        headers[7] = "JoinerName";
        completedList.add(headers);
        uncompletedList.add(headers);

        String uri = thirdServiceEndpoint + "/task-report/%s";
        var restTemplate = new RestTemplate();
        uri = String.format(uri,joinerId);

        ResponseEntity<FullTaskResultPojo> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<FullTaskResultPojo>() {}
        );

        FullTaskResultPojo taskPojoMap = responseEntity.getBody();

        taskPojoMap.getFinished().forEach( task -> {
            String[] results = new String[8];
            results[0] = task.getId().toString();
            results[1] = task.getName();
            results[2] = task.getDescription();
            results[3] = task.getEstimatedRequiredHours().toString();
            results[4] = task.getStack();
            results[5] = task.getParentTask() == null?"":task.getParentTask().toString();
            results[6] = task.getJoinerInCharge().getId().toString();
            results[7] = task.getJoinerInCharge().getName();
            completedList.add(results);
        });
        try{
            CSVWriter writer = new CSVWriter(new FileWriter(completedBydefinedJoinerPathFile));
            writer.writeAll(completedList);
            writer.close();
        }catch (Exception exception){
            exception.printStackTrace();
            return "Unable to write file";
        }

        taskPojoMap.getUnfinished().forEach( task -> {
            String[] results = new String[8];
            results[0] = task.getId().toString();
            results[1] = task.getName();
            results[2] = task.getDescription();
            results[3] = task.getEstimatedRequiredHours().toString();
            results[4] = task.getStack();
            results[5] = task.getParentTask() == null?"":task.getParentTask().toString();
            results[6] = task.getJoinerInCharge().getId().toString();
            results[7] = task.getJoinerInCharge().getName();
            uncompletedList.add(results);
        });

        try{
            CSVWriter writer = new CSVWriter(new FileWriter(uncompletedBydefinedJoinerPathFile));
            writer.writeAll(uncompletedList);
            writer.close();
        }catch (Exception exception){
            exception.printStackTrace();
            return "Unable to write file";
        }


        return "Successfully created the report";
    }

    public String timeReportGenerator(){
        String[] headers = new String[3];
        List<String[]> stringList = new ArrayList<>();

        headers[0] = "JoinerId";
        headers[1] = "Days";
        headers[2] = "Hours";
        stringList.add(headers);


        String uri = thirdServiceEndpoint + "/time-to-complete";
        var restTemplate = new RestTemplate();
        uri = String.format(uri);

        ResponseEntity<List<DaysLeftToCompleteTaskPojo>> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DaysLeftToCompleteTaskPojo>>() {}
        );

        List<DaysLeftToCompleteTaskPojo> daysLeftToCompleteTaskList = responseEntity.getBody();

        daysLeftToCompleteTaskList.forEach(timeReport -> {
            String[] results = new String[3];
            results[0] = timeReport.getJoinerId().toString();
            results[1] = timeReport.getDays().toString();
            results[2] = timeReport.getHours().toString();
            stringList.add(results);
        });
        try{
            CSVWriter writer = new CSVWriter(new FileWriter(timeLeftPathFile));
            writer.writeAll(stringList);
            writer.close();
        }catch (Exception exception){
            exception.printStackTrace();
            return "Unable to write file";
        }
        return "Successfully created the report";
    }
}
