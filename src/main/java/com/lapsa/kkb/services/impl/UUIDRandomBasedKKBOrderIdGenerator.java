package com.lapsa.kkb.services.impl;

import java.util.UUID;

import javax.ejb.Singleton;

import com.lapsa.kkb.api.KKBOrderIdGenerator;

@Singleton
public class UUIDRandomBasedKKBOrderIdGenerator implements KKBOrderIdGenerator {

    @Override
    public String generateOrderId() {
	UUID uuid = UUID.randomUUID();
	long lng = Math.abs(uuid.getLeastSignificantBits() / 10000);
	String id = String.format("%015d", lng);
	return id;
    }

    public static void main(String[] args) {
	UUIDRandomBasedKKBOrderIdGenerator generator = new UUIDRandomBasedKKBOrderIdGenerator();
	System.out.println(generator.generateOrderId());
	System.out.println(generator.generateOrderId());
	System.out.println(generator.generateOrderId());
	System.out.println(generator.generateOrderId());
    }
}
