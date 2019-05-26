package com.assignment.shareapp.controller;

import com.assignment.shareapp.exception.ResourceNotFoundException;
import com.assignment.shareapp.model.Group;
import com.assignment.shareapp.model.Member;
import com.assignment.shareapp.model.PaymentActivity;
import com.assignment.shareapp.repository.GroupRepository;
import com.assignment.shareapp.repository.MemberRepository;
import com.assignment.shareapp.repository.PaymentActivityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class PaymentActivityController {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    MemberRepository memberRepository;
    
    @Autowired
    PaymentActivityRepository paymentActivityRepository;

    // Get All PaymentActivities
    @GetMapping("/payactivities")
    public List<PaymentActivity> getAllPaymentActivity() {
        return paymentActivityRepository.findAll();
    }

    // Get a Single PaymentActivity
    @GetMapping("/payactivities/{id}")
    public PaymentActivity getPaymentActivityById(@PathVariable(value = "id") Integer pId) {
        return paymentActivityRepository.findById(pId)
            .orElseThrow(() -> new ResourceNotFoundException("Group", "id", pId));
    }

    // Create a new PaymentActivity
    @PostMapping("/payactivities")
    public PaymentActivity createPaymentActivity(@Valid @RequestBody PaymentActivity payactivity) {
        return paymentActivityRepository.save(payactivity);
    }

    // Update a PaymentActivity
    @PutMapping("/payactivities/{id}")
    public PaymentActivity updatePaymentActivity(@PathVariable(value = "id") Integer pId,
                             @Valid @RequestBody PaymentActivity payActivityDetails) {

    	PaymentActivity paymentActivity = paymentActivityRepository.findById(pId)
            .orElseThrow(() -> new ResourceNotFoundException("Group", "id", pId));

    	if(payActivityDetails.getActivityCode() != null)
        	paymentActivity.setActivityCode(payActivityDetails.getActivityCode());
        	
    	if(payActivityDetails.getActivityDescription() != null)
    	paymentActivity.setActivityDescription(payActivityDetails.getActivityDescription());
    	
    	if(payActivityDetails.getAmount() != 0)
    	paymentActivity.setAmount(payActivityDetails.getAmount());
    	
    	if(payActivityDetails.getMember() != null)
    	paymentActivity.setMember(payActivityDetails.getMember());
    	
    	if(payActivityDetails.getTransWithMembers() != null)
    	paymentActivity.setTransWithMembers(payActivityDetails.getTransWithMembers());
    	
    	if(payActivityDetails.getGroup() != null)
    	paymentActivity.setGroup(payActivityDetails.getGroup());

        PaymentActivity updatedPaymentActivity = paymentActivityRepository.save(paymentActivity);
        return updatedPaymentActivity;
    }

    // Delete a PaymentActivity
    @DeleteMapping("/payactivities/{id}")
    public ResponseEntity<?> deletePaymentActivity(@PathVariable(value = "id") Integer pId) {
    	PaymentActivity paymentActivity = paymentActivityRepository.findById(pId)
            .orElseThrow(() -> new ResourceNotFoundException("Group", "id", pId));

        paymentActivityRepository.delete(paymentActivity);

        return ResponseEntity.ok().build();
    }
    
    // Get a All PaymentActivities for group id
    @GetMapping("/payactivities/groups/{id}")
    public List<PaymentActivity> getPaymentActivityByGroupId(@PathVariable(value = "id") Integer groupId) {
  	
		  //Not an optimized code. Criteria or HQL would be better
		  return  paymentActivityRepository.findAll().stream().filter(pa->pa.getGroup().getGroupId()==groupId).collect(Collectors.toList()); 
		 
    }
    
    // Get a All PaymentActivities for group id
    @GetMapping("/payactivities/summary/groups/{id}")
    public List<String> getPaymentSummryforMemberByGroupId(@PathVariable(value = "id") Integer groupId, @RequestParam(required = true, name = "memberId") Integer memberId) {
  	
		  //Not an optimized code. Criteria or HQL would be better
    	  List<PaymentActivity> calcAl = paymentActivityRepository.findAll().stream()
    			  													.filter(pa->pa.getGroup().getGroupId()==groupId)
    			  														.collect(Collectors.toList()); 
		  
    	  //fetch the group which the member belongs to
		  Group memberGroup = groupRepository.findById(groupId)
		            .orElseThrow(() -> new ResourceNotFoundException("Group", "id", groupId));
    	  
		  //Fetch the Member
		  Member member = memberRepository.findById(memberId)
		            .orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId));
		  
		  Map<Integer,Float> memberExpns = calculatePaybleToIndividual(member,memberGroup,calcAl);
		  
		  List<String> result = new ArrayList<String>();
		  for(Map.Entry<Integer,Float> mpEntr: memberExpns.entrySet()) {				
			if(mpEntr.getKey() == member.getMemberId())
			{
				if(mpEntr.getValue() > 0)
					result.add(" SUMMARY: Total amount to be received by "+ mpEntr.getKey() +" is:"+ mpEntr.getValue());
				if(mpEntr.getValue() < 0 )
					result.add(" SUMMARY: Total amount to be paid by "+ mpEntr.getKey() +" is:"+ -mpEntr.getValue());
			}
			else {
				if(mpEntr.getValue() > 0)
					result.add(" Amount to be received from "+ mpEntr.getKey() +" by "+ member.getMemberId()+ " is:"+ mpEntr.getValue());
				if(mpEntr.getValue() < 0 )
					result.add(" Amount to be paid to "+ mpEntr.getKey() +" by "+ member.getMemberId()+ " is:"+ -mpEntr.getValue());
			}				
		  }	
		  
    	  return result;		 
    }
    
	public static Map<Integer,Float> calculatePaybleToIndividual(Member member,Group group,List<PaymentActivity> calcAl) {
		
		float totalGrpExternalExpenses = 0;
		float totalIndExternalExpenses = 0;
		float totalPaid = 0;
		float totalReceived = 0;
		Map<Integer,Float> indExpns = new HashMap<Integer,Float>();
		int divisor = group.getMembers().size();
		
		for(PaymentActivity pa: calcAl) {							
			if(group.getGroupId() == pa.getGroup().getGroupId()) {
				
				//In the current scenario any EXTERNAL transaction is considered to spent for whole Group
				if(PaymentActivity.ActivityCode.EXTERNAL.equals(pa.getActivityCode()) && 
						member.getMemberId() != pa.getMember().getMemberId()) {
					totalGrpExternalExpenses+= pa.getAmount();	
					
					float total = indExpns.get(pa.getMember().getMemberId()) == null ? 0 : indExpns.get(pa.getMember().getMemberId());
					indExpns.put(pa.getMember().getMemberId(), total - (pa.getAmount()/divisor));				
				}
				
				if(PaymentActivity.ActivityCode.EXTERNAL.equals(pa.getActivityCode()) && 
						member.getMemberId() == pa.getMember().getMemberId()) {
					totalIndExternalExpenses += pa.getAmount();				
				}
				//In the current scenario any INTERNAL transaction is between 1:1 
				else if(PaymentActivity.ActivityCode.INTERNAL.equals(pa.getActivityCode()) &&
						member.getMemberId() == pa.getMember().getMemberId()) {
					totalPaid += pa.getAmount();
					
					float total = indExpns.get(pa.getTransWithMembers().get(0).getMemberId()) == null ? 0 : indExpns.get(pa.getTransWithMembers().get(0).getMemberId());
					indExpns.put(pa.getTransWithMembers().get(0).getMemberId(), total + pa.getAmount());
				}	
				else if(PaymentActivity.ActivityCode.INTERNAL.equals(pa.getActivityCode()) && 
							member.getMemberId() == pa.getTransWithMembers().get(0).getMemberId()) {					
					totalReceived += pa.getAmount();
					
					float total = indExpns.get(pa.getTransWithMembers().get(0).getMemberId()) == null ? 0 : indExpns.get(pa.getTransWithMembers().get(0).getMemberId());
					indExpns.put(pa.getMember().getMemberId(), total - pa.getAmount());
				}
			}
		}
		
		float paidtoOthers= totalIndExternalExpenses/divisor;
		for(Member m: group.getMembers()) {
			if(indExpns.get(m.getMemberId()) == null) {
				indExpns.put(m.getMemberId(),paidtoOthers);
			}
			else {
				indExpns.put(m.getMemberId(),indExpns.get(m.getMemberId()) + paidtoOthers);
			}
		}
				
		totalGrpExternalExpenses += totalIndExternalExpenses;
		float summaryTotal = (totalIndExternalExpenses+totalPaid-totalReceived) - (totalGrpExternalExpenses/divisor); 	
		//Member's Summary of Total amount to pay or receive is stored against its ID
		indExpns.put(member.getMemberId(), summaryTotal);
		
		return indExpns;		
	}

}
