package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.service.PartService;
import com.example.demo.service.PartServiceImpl;
import com.example.demo.service.ProductService;
import com.example.demo.service.ProductServiceImpl;

@Controller
public class AddProductController {
	@Autowired
	private ApplicationContext context;
	private PartService partService;
	@SuppressWarnings("unused")
	private List<Part> theParts;
	private static Product product1;
	private Product product;

	@GetMapping("/showFormAddProduct")
	public String showFormAddPart(Model theModel) {
		theModel.addAttribute("parts", partService.findAll());
		product = new Product();
		product1 = product;
		theModel.addAttribute("product", product);

		List<Part> availParts = new ArrayList<>();
		for (Part p : partService.findAll()) {
			if (!product.getParts().contains(p))
				availParts.add(p);
		}
		theModel.addAttribute("availparts", availParts);
		theModel.addAttribute("assparts", product.getParts());
		return "productForm";
	}

	@PostMapping("/showFormAddProduct")
	public String submitForm(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult,
			Model theModel, RedirectAttributes ra) {
		theModel.addAttribute("product", product);

		if (bindingResult.hasErrors()) {
			ProductService productService = context.getBean(ProductServiceImpl.class);
			Product product2 = new Product();
			try {
				product2 = productService.findById((int) product.getId());
			} catch (Exception e) {
				System.out.println("Error Message " + e.getMessage());
			}
			theModel.addAttribute("parts", partService.findAll());
			List<Part> availParts = new ArrayList<>();
			for (Part p : partService.findAll()) {
				if (!product2.getParts().contains(p))
					availParts.add(p);
			}
			theModel.addAttribute("availparts", availParts);
			theModel.addAttribute("assparts", product2.getParts());
			return "productForm";
		} else {

			Integer newProductInv = product.getInv();

			for (Part p : product.getParts()) {
				Integer partInv = p.getInv();
				Integer partMin = p.getMinInv();

				if (partInv == null || partMin == null || newProductInv == null)
					continue;

				int projected = partInv - newProductInv;

				if (projected < partMin) {
					bindingResult.reject("product.lowpartinventory",
							"Setting this product inventory to " + newProductInv + " would lower part '" + p.getName()
									+ "' below its minimum inventory (" + partMin + ").");
					break;
				}
			}

			if (bindingResult.hasErrors()) {
				theModel.addAttribute("parts", partService.findAll());
				List<Part> availParts = new ArrayList<>();
				for (Part p : partService.findAll()) {
					if (!product.getParts().contains(p))
						availParts.add(p);
				}
				theModel.addAttribute("availparts", availParts);
				theModel.addAttribute("assparts", product.getParts());
				return "productForm";
			}

			ProductService repo = context.getBean(ProductServiceImpl.class);
			if (product.getId() != 0) {
				Product product2 = repo.findById((int) product.getId());
				PartService partService1 = context.getBean(PartServiceImpl.class);
				if (product.getInv() - product2.getInv() > 0) {
					for (Part p : product2.getParts()) {
						int inv = p.getInv();
						p.setInv(inv - (product.getInv() - product2.getInv()));
						partService1.save(p);
					}
				}
			} else {
				product.setInv(0);
			}
			repo.save(product);
			ra.addFlashAttribute("msg", "Product saved successfully.");
			return "redirect:/mainscreen";
		}
	}

	@GetMapping("/showProductFormForUpdate")
	public String showProductFormForUpdate(@RequestParam("productID") int theId, Model theModel) {
		theModel.addAttribute("parts", partService.findAll());
		ProductService repo = context.getBean(ProductServiceImpl.class);
		Product theProduct = repo.findById(theId);
		product1 = theProduct;

		// set as a model attribute to pre-populate the form
		theModel.addAttribute("product", theProduct);
		theModel.addAttribute("assparts", theProduct.getParts());
		List<Part> availParts = new ArrayList<>();
		for (Part p : partService.findAll()) {
			if (!theProduct.getParts().contains(p))
				availParts.add(p);
		}
		theModel.addAttribute("availparts", availParts);
		// send over to our form
		return "productForm";
	}

	@GetMapping("/deleteproduct")
	public String deleteProduct(@RequestParam("productID") int theId, Model theModel, RedirectAttributes ra) {
		ProductService productService = context.getBean(ProductServiceImpl.class);
		Product product2 = productService.findById(theId);
		for (Part part : product2.getParts()) {
			part.getProducts().remove(product2);
			partService.save(part);
		}
		product2.getParts().removeAll(product2.getParts());
		productService.save(product2);
		productService.deleteById(theId);

		ra.addFlashAttribute("msg", "Product deleted successfully.");
		return "redirect:/mainscreen";
	}

	public AddProductController(PartService partService) {
		this.partService = partService;
	}

	// make the add and remove buttons work
	@GetMapping("/associatepart")
	public String associatePart(@Valid @RequestParam("partID") int theID, Model theModel, RedirectAttributes ra) {
		if (product1.getName() == null) {
			ra.addFlashAttribute("err", "Please save the product before adding parts.");
			return "redirect:/showFormAddProduct";
		} else {
			product1.getParts().add(partService.findById(theID));
			partService.findById(theID).getProducts().add(product1);
			ProductService productService = context.getBean(ProductServiceImpl.class);
			productService.save(product1);
			partService.save(partService.findById(theID));
			theModel.addAttribute("product", product1);
			theModel.addAttribute("assparts", product1.getParts());
			List<Part> availParts = new ArrayList<>();
			for (Part p : partService.findAll()) {
				if (!product1.getParts().contains(p))
					availParts.add(p);
			}
			theModel.addAttribute("availparts", availParts);
			return "productForm";
		}
	}

	@GetMapping("/removepart")
	public String removePart(@RequestParam("partID") int theID, Model theModel) {
		theModel.addAttribute("product", product);
		product1.getParts().remove(partService.findById(theID));
		partService.findById(theID).getProducts().remove(product1);
		ProductService productService = context.getBean(ProductServiceImpl.class);
		productService.save(product1);
		partService.save(partService.findById(theID));
		theModel.addAttribute("product", product1);
		theModel.addAttribute("assparts", product1.getParts());
		List<Part> availParts = new ArrayList<>();
		for (Part p : partService.findAll()) {
			if (!product1.getParts().contains(p))
				availParts.add(p);
		}
		theModel.addAttribute("availparts", availParts);
		return "productForm";
	}
}
