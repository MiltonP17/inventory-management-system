package com.example.demo.controllers;

import com.example.demo.domain.InhousePart;
import com.example.demo.service.InhousePartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AddInhousePartController {
    @Autowired
    private final InhousePartService inhousePartService;

    public AddInhousePartController(InhousePartService inhousePartService) {
        this.inhousePartService = inhousePartService;
    }

    @GetMapping("/showFormAddInPart")
    public String showFormAddInhousePart(Model theModel) {
        InhousePart inhousepart = new InhousePart();
        theModel.addAttribute("inhousepart", inhousepart);
        return "InhousePartForm";
    }

    @SuppressWarnings("unused")
    @PostMapping("/showFormAddInPart")
    public String submitForm(@Valid @ModelAttribute("inhousepart") InhousePart part, BindingResult binding, Model model,
            RedirectAttributes ra) {
        model.addAttribute("inhousepart", part);
        model.addAttribute("inhousepart", part);

        // 1) JSR-303 field validation errors?
        if (binding.hasErrors()) {
            return "InhousePartForm";
        }

        // 2) Cross-field validation for Part G
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
                // you can also add a global error via binding.reject("range", ...)
                binding.rejectValue("minInv", "range", "Min must be less than or equal to Max.");
                binding.rejectValue("maxInv", "range", "Max must be greater than or equal to Min.");
            }
            if (inv < min) {
                binding.rejectValue("inv", "range", "Inventory must be greater than or equal to Min.");
            }
            if (inv > max) {
                binding.rejectValue("inv", "range", "Inventory must be less than or equal to Max.");
            }
        }

        if (binding.hasErrors()) {
            return "InhousePartForm";
        }

        // 3) Save (no lookup by id on ADD)
        inhousePartService.save(part);

        // 4) Success
        ra.addFlashAttribute("msg", "Inhouse part saved successfully.");
        return "redirect:/mainscreen";

    }

}
