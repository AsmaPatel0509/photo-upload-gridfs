package com.ibm.photo_upload_gridfs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class App {
	public static void main(String[] args) throws IOException {
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("hr");
		
		// Save a image in DB
		saveImageIntoMongoDB(db);
		
		// Get a image from DB
		getSingleImageExample(db);
		
		// Get all images from DB
		listAllImages(db);
		saveToFileSystem(db);
		
		// Delete images from DB
		deleteImageFromMongoDB(db);
		
		// Verifying if image was deleted or not
		getSingleImageExample(db);
	}

	private static void saveImageIntoMongoDB(DB db) throws IOException {
		
		
		URL url = new URL("https://p.bigstockphoto.com/GeFvQkBbSLaMdpKXF1Zv_bigstock-Aerial-View-Of-Blue-Lakes-And--227291596.jpg"); //insert url of your image
		BufferedImage imageFile = ImageIO.read(url);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(imageFile, "jpg", bos );
	    byte [] data = bos.toByteArray();
		
	    String dbFileName = "DemoImage";
		
		GridFS gfsPhoto = new GridFS(db, "photo");
		GridFSInputFile gfsFile = gfsPhoto.createFile(data);
		System.out.println("Image added to db");
		gfsFile.setFilename(dbFileName);
		gfsFile.save();
	}

	private static void getSingleImageExample(DB db) {
		String newFileName = "/home/...";  //insert path here
		
		GridFS gfsPhoto = new GridFS(db, "photo");
		GridFSDBFile imageForOutput = gfsPhoto.findOne(newFileName);
		System.out.println(imageForOutput);
	}

	private static void listAllImages(DB db) {
		GridFS gfsPhoto = new GridFS(db, "photo");
		DBCursor cursor = gfsPhoto.getFileList();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
	}

	private static void saveToFileSystem(DB db) throws IOException {
		String dbFileName = "DemoImage";
		GridFS gfsPhoto = new GridFS(db, "photo");
		GridFSDBFile imageForOutput = gfsPhoto.findOne(dbFileName);
		imageForOutput.writeTo("/home/ibm-wave10-<your-name>/Desktop/urlImage.jpg"); //insert path where you want to store the image
	}

	private static void deleteImageFromMongoDB(DB db) {
		String dbFileName = "DemoImage";
		GridFS gfsPhoto = new GridFS(db, "photo");
		gfsPhoto.remove(gfsPhoto.findOne(dbFileName));
	}
}

/* this will create two collections: photo.files and photo.chunks  */ 

