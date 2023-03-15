package com.spring.cemstools.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.cemstools.generacion.GeneraAlarmas;
import com.spring.cemstools.generacion.GeneraCfgSist;
import com.spring.cemstools.generacion.GeneraCfgWeb;
import com.spring.cemstools.generacion.GeneraHoldingRegister;

@Controller
@RequestMapping("/cemstools")
public class PLCFileGenerator {

	@GetMapping("/")
	public String index(){
		return "index";
	}
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public String uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("opcion") int opc, Model model){
		boolean res = true;
		List<String> info = new LinkedList<String>();
		String text = "";
		
		if(file!=null) {
			BufferedReader br;
			try {

			     String line;
			     InputStream is = file.getInputStream();
			     br = new BufferedReader(new InputStreamReader(is));
			     while ((line = br.readLine()) != null) {
			    	 info.add(line);
			     }
			     
			     if(info.size()>1){
			    	 res = false;
			    	 
			    	 switch (opc) {
						case 1: {
							text = GeneraAlarmas.formateaString(info);
							break;
						}
						case 2: {
							text = GeneraCfgSist.formateaString(info);
							break;
						}
						case 3: {
							text = GeneraCfgWeb.formateaString(info);
							break;
						}
						case 4: {
							text = GeneraHoldingRegister.formateaString(info);
							break;
						}
						default:
							res = true;
							throw new IllegalArgumentException("Unexpected value: " + opc);
						}
			    	 
			     }		

				

			  } catch (IOException e) {
			    System.err.println(e.getMessage());   
			  }
		}
			
		model.addAttribute("error",res);
		model.addAttribute("text",text);
		return "result";
	}
}
