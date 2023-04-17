package com.example.CafeManagementbackend.serviceImpl;

import com.example.CafeManagementbackend.error.Cafeutils;
import com.example.CafeManagementbackend.jwt.JwtFilter;
import com.example.CafeManagementbackend.model.Bill;
import com.example.CafeManagementbackend.repo.BillRepo;
import com.example.CafeManagementbackend.service.BillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillServiceImpl implements BillService {
    @Autowired
    private BillRepo billRepo;

    @Autowired
    private JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> generateBill(Map<String, Object> requestMap) {
        try{
            String filename;
            if( this.validateRequestMap(requestMap)){
               if(requestMap.containsKey("isGenerate")&& !(Boolean) requestMap.get("isGenerate") ){
                   filename= (String) requestMap.get("uuid");
               }
               else{
                   filename= Cafeutils.getUUID();
                   requestMap.put("uuid", filename);
                   this.insertBill(requestMap);
               }

               return new ResponseEntity<>("Created",HttpStatus.OK);
            }
            else{
                new ResponseEntity<>("Invalid Data",HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void insertBill(Map<String, Object> requestMap) {
        try{
            Bill bill = new Bill();
            bill.setUuid((String)requestMap.get("uuid"));
            bill.setFirstName((String)requestMap.get("firstName"));
            bill.setLastName((String)requestMap.get("lastName"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setPaymentMethod((String)requestMap.get("paymentMethod"));
            bill.setTotalAmount(Integer.parseInt((String)requestMap.get("totalAmount")));
            bill.setProductDetail((String) requestMap.get("productDetail"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billRepo.save(bill);

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("firstName")&&
                requestMap.containsKey("lastName")&&
                requestMap.containsKey("email")&&
                requestMap.containsKey("paymentMethod")&&
                requestMap.containsKey("totalAmount")&&
                requestMap.containsKey("productDetail");


    }


}

