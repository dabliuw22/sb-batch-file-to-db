package com.leysoft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leysoft.service.inter.ChunkService;

@RestController
@RequestMapping(value = {"/chunk"})
public class ChunkController {

	@Autowired
	private ChunkService chunkService;
	
	@GetMapping(value = {"/run"})
	public ResponseEntity<String> run() {
		chunkService.run();
		return ResponseEntity.ok("Ok");
	}
}
