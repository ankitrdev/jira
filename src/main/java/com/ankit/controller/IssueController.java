package com.ankit.controller;

import com.ankit.DTO.IssueDTO;
import com.ankit.modal.Issue;
import com.ankit.modal.User;
import com.ankit.request.IssueRequest;
import com.ankit.response.AuthResponse;
import com.ankit.response.MessageResponse;
import com.ankit.service.IssueService;
import com.ankit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {
    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @GetMapping("/{issueId}")
    public ResponseEntity<Issue> getIssueById(@PathVariable Long issueId ) throws Exception {
       return new ResponseEntity<>(issueService.getIssueById(issueId), HttpStatus.OK);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Issue>> getIssueByProjectId(@PathVariable Long projectId) throws Exception{
        return new ResponseEntity<>(issueService.getIssueByProjectId(projectId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<IssueDTO> createIssue(
            @RequestBody IssueRequest issueRequest,
            @RequestHeader("Authorization") String token
            ) throws Exception {
        System.out.println("issue" + issueRequest);
        User tokenUser = userService.findUserProfileByJwt(token);
        User user = userService.findUserById(tokenUser.getId());

        Issue createIssue = issueService.createIssue(issueRequest, user);
        IssueDTO issueDTO = new IssueDTO();
        issueDTO.setDescription(createIssue.getDescription());
        issueDTO.setDueDate(createIssue.getDueDate());
        issueDTO.setId(createIssue.getId());
        issueDTO.setPriority(createIssue.getPriority());
        issueDTO.setProject(createIssue.getProject());
        issueDTO.setProjectID(createIssue.getProjectID());
        issueDTO.setTitle(createIssue.getTitle());
        issueDTO.setTags(createIssue.getTags());
        issueDTO.setAssignee(createIssue.getAssignee());

        return ResponseEntity.ok(issueDTO);

    }


    @DeleteMapping("/{issueId}")
    public ResponseEntity<MessageResponse> deleteIssue(
            @PathVariable Long issueId,
            @RequestHeader("Authorization") String token
        ) throws Exception {
        User user = userService.findUserProfileByJwt(token);
        issueService.deleteIssue(issueId, user.getId());

        MessageResponse res = new MessageResponse();
        res.setMessage("Issue Deleted");

        return ResponseEntity.ok(res);
    }

    @PutMapping("/{issueId}/assignee/{userId}")
    public ResponseEntity<Issue> addUserToIssue(@PathVariable Long issueId,
                                                @PathVariable Long userId) throws Exception {
        Issue issue = issueService.addUserToIssue(issueId, userId);
        return ResponseEntity.ok(issue);
    }

    @PutMapping("/{issueId}/status/{status}")
    public ResponseEntity<Issue> updateIssueStatus(@PathVariable String status,
                                                   @PathVariable Long issueId) throws Exception{
        Issue issues = issueService.updateStatus(issueId, status);
        return ResponseEntity.ok(issues);
    }

}