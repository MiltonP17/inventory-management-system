package com.example.demo.controllers;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.service.PartService;
import com.example.demo.service.ProductService;

@Controller
public class MainScreenController {

	private PartService partService;
	private ProductService productService;
	private List<Part> theParts;
	private List<Product> theProducts;

	public MainScreenController(PartService partService, ProductService productService) {
		this.partService = partService;
		this.productService = productService;
	}

	@GetMapping("/mainscreen")
	public String listPartsandProducts(Model theModel, @Param("partkeyword") String partkeyword,
			@Param("productkeyword") String productkeyword) {

		// add to the sprig model
		List<Part> partList = partService.listAll(partkeyword);
		theModel.addAttribute("parts", partList);
		theModel.addAttribute("partkeyword", partkeyword);

		List<Product> productList = productService.listAll(productkeyword);
		theModel.addAttribute("products", productList);
		theModel.addAttribute("productkeyword", productkeyword);
		return "mainscreen";
	}

	@PostMapping("/product/{id}/buy")
	public String buyProduct(@PathVariable("id") Long id, RedirectAttributes ra) {
		boolean ok = productService.buyOne(id);
		if (ok) {
			ra.addFlashAttribute("msg", "Purchase successful.");
		} else {
			ra.addFlashAttribute("err", "Purchase failed: out of stock or product not found.");
		}
		return "redirect:/mainscreen";
	}

}
