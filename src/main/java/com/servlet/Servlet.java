package com.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import com.google.gson.Gson;
import com.servlet.model.Item;
import com.servlet.repository.ItemRepository;

@WebServlet("/items/*")
public class Servlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ItemRepository itemRepository;
	private Gson gson;

	public Servlet() {
		super();

		this.itemRepository = new ItemRepository();
		this.gson = new Gson();

		// fixtures
		this.itemRepository.add(new Item(UUID.randomUUID(), "First"));
		this.itemRepository.add(new Item(UUID.randomUUID(), "Second"));
	}

	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		super.service(request, response);

		response.setContentType("application/json");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		if (path == null || path.equals("/")) {
			Collection<Item> items = this.itemRepository.all();

			String body = gson.toJson(items);

			response.getWriter().append(body);
			return;
		}

		try {
			UUID id = UUID.fromString(path.replace("/", ""));
			;
			Item item = itemRepository.get(id);

			response.getWriter().append(gson.toJson(item));
			return;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!request.getContentType().equals("application/json")) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		try {
			Item input = this.gson.fromJson(request.getReader(), Item.class);
			Item item = new Item(UUID.randomUUID(), input.getData());

			this.itemRepository.add(item);

			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().append(gson.toJson(item));
			return;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!request.getContentType().equals("application/json")) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		try {
			UUID id = UUID.fromString(request.getPathInfo().replace("/", ""));
			Item input = this.gson.fromJson(request.getReader(), Item.class);
			Item item = itemRepository.get(id);

			item.setData(input.getData());

			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().append(gson.toJson(item));
			return;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			UUID id = UUID.fromString(request.getPathInfo().replace("/", ""));
			Item item = itemRepository.get(id);
			itemRepository.remove(item);

			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	}
}
