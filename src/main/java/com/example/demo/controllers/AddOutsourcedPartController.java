package com.example.demo.controllers;

import com.example.demo.domain.OutsourcedPart;
import com.example.demo.domain.Part;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;

@Controller
public class AddOutsourcedPartController {
    @Autowired
    private final OutsourcedPartService outsourcedPartService;

    public AddOutsourcedPartController(OutsourcedPartService outsourcedPartService) {
        this.outsourcedPartService = outsourcedPartService;
    }

    @GetMapping("/showFormAddOutPart")
    public String showFormAddOutsourcedPart(Model theModel){
        Part part=new OutsourcedPart();
        theModel.addAttribute("outsourcedpart",part);
        return "OutsourcedPartForm";
    }

    @SuppressWarnings("unused")
	@PostMapping("/showFormAddOutPart")
    public String submitForm(@Valid @ModelAttribute("outsourcedpart") OutsourcedPart part, BindingResult binding, Model theModel){
        theModel.addAttribute("outsourcedpart",part);
        // ---- 1) Bean Validation errors? Back to form.
        if (binding.hasErrors()) {
            return "OutsourcedPartForm";
        }

        // ---- 2) Cross-field validation (Part G)
        Integer min = part.getMinInv();
        Integer max = part.getMaxInv();
        Integer inv = part.getInv();

        if (min == null) {
            binding.rejectValue("minInv", "minInv.required", "Minimum inventory is required.");
        }
        if (max == null) {
            binding.rejectValue("maxInv", "maxInv.required", "Maximum inventory is required.");
        }
        if (inv == null) {
            binding.rejectValue("inv", "inv.required", "Inventory is required.");
        }

        if (!binding.hasErrors()) {
            if (min > max) {
                binding.rejectValue("minInv", "range", "Min must be ≤ Max.");
                binding.rejectValue("maxInv", "range", "Max must be ≥ Min.");
            }
            if (inv < min) {
                binding.rejectValue("inv", "range", "Inventory must be ≥ Min.");
            }
            if (inv > max) {
                binding.rejectValue("inv", "range", "Inventory must be ≤ Max.");
            }
        }

        if (binding.hasErrors()) {
            return "OutsourcedPartForm";
        }

        // ---- 3) Save new outsourced part
        outsourcedPartService.save(part);

        // ---- 4) Success view
        return "confirmationaddpart";
    }



}
