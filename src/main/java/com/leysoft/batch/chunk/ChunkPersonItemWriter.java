package com.leysoft.batch.chunk;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.leysoft.model.Person;

@Component(value = "chunkPersonItemWriter")
public class ChunkPersonItemWriter implements ItemWriter<Person> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ChunkPersonItemWriter.class);

	@Override
	public void write(List<? extends Person> items) throws Exception {
		items.forEach(person -> LOGGER.info("person -> {}", person));
	}
}
