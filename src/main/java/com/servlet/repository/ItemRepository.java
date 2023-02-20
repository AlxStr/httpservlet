package com.servlet.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.servlet.model.Item;

public class ItemRepository {
	private Map<UUID, Item> storage = new HashMap<UUID, Item>();	
	
	public Collection<Item> all() {
		return this.storage.values();
	}
	
	public void add(Item resource) {
		if (this.storage.containsKey(resource.getId())) {
			throw new IllegalArgumentException("Item is already exists");
		}

		storage.put(resource.getId(), resource);
	}
	
	
	public Item get(UUID id) {
		if (!this.storage.containsKey(id)) {
			throw new IllegalArgumentException("Item not found");
		}
		
		return this.storage.get(id);
	}

	public void remove(Item item) {
		this.storage.remove(item.getId());
	}
}
