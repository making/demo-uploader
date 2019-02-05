package com.example.demouploader;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import static java.util.stream.Collectors.toList;

@Controller
public class UploaderController {

	private final UploaderProps props;

	private final ResourcePatternResolver resourcePatternResolver;

	public UploaderController(UploaderProps props,
			ResourcePatternResolver resourcePatternResolver) {
		this.props = props;
		this.resourcePatternResolver = resourcePatternResolver;
	}

	@GetMapping(path = "/")
	public String index(Model model) throws IOException {
		Resource[] resources = this.resourcePatternResolver
				.getResources(this.props.fileSystemPathPattern());
		List<File> files = Stream.of(resources).filter(x -> {
			try {
				return x.getFile().isFile();
			}
			catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}).map(resource -> {
			try {
				return resource.getFile();
			}
			catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}).collect(toList());
		model.addAttribute("files", files);
		return "index";
	}

	@PostMapping(path = "/upload")
	public String upload(@RequestParam("file") MultipartFile multipartFile)
			throws IOException {
		String filename = multipartFile.getOriginalFilename();
		if (!StringUtils.isEmpty(filename)) {
			multipartFile.transferTo(new File(this.props.getDir() + "/" + filename));
		}
		return "redirect:/";
	}

}
