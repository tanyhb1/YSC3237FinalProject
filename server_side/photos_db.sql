--
-- PostgreSQL database dump
--

-- Dumped from database version 11.4
-- Dumped by pg_dump version 11.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;



CREATE TABLE public.users (
    id serial PRIMARY KEY,
    name text,
    photo text
);

CREATE TABLE public.photos (
    id serial PRIMARY KEY,
    created_at TIMESTAMP DEFAULT NOW(),
    long double precision NOT NULL,
    lat double precision NOT NULL,
    photo text, 
    creator INTEGER 
    );
ALTER TABLE ONLY public.photos
    ADD  FOREIGN KEY (creator) REFERENCES public.users(id);



