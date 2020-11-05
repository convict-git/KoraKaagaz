package processing.test;
import org.junit.Test;
import processing.utility.*;
import java.util.*;
import static org.junit.Assert.*;

/**
 *
 * @author Sakshi Rathore
 */

public class TestUtility {

   @Test 
   public void testPosition() {
	   int col = 1;
	   int row = 2;
	   
	   Position pos = new Position(row, col);
	   assertEquals(pos.r, row);
	   assertEquals(pos.c, col);
	   
	   Position copy = new Position(pos);
	   assertEquals(copy.r, pos.r);
	   assertEquals(copy.c, pos.c);
	   assertTrue(copy.equals(pos));
	   
	   Position falseCopy = new Position(1,3);
	   assertFalse(falseCopy.equals(copy));
	   
   }
   
   @Test
   public void testDimension() {
	   Dimension dim = new Dimension(100, 200);
	   assertEquals(dim.numRows, 100);
	   assertEquals(dim.numCols, 200);
	   
	   Dimension copy = new Dimension(dim);
	   assertTrue(copy.equals(dim));
	   
	   Dimension falseCopy = new Dimension(200, 100);
	   assertFalse(falseCopy.equals(dim));
   }
   
   @Test
   public void testAngle() {
	   
	   Angle angle = new Angle(12.23);
//	   assertEquals(angle.angle, 12.23); 
	   
   }
   
   @Test 
   public void testFilePath() {
	   String path = "/src/processsing/test/TestUtility.java";
	   Filepath filePath = new Filepath(path);
	   assertEquals(filePath.toString(), path);
	   
	   Filepath copy = new Filepath(filePath);
	   assertEquals(copy.toString(), filePath.toString());
	   
	   Filepath falseCopy = new Filepath("/src/processing/utility");
	   assertFalse(falseCopy.equals(copy));
	   
   }
   
   @Test 
   public void testIntensity() {
	   int r = 1;
	   int g = 2;
	   int b = 3;
	   
	   Intensity intensity  = new Intensity(r, g, b);
	   assertEquals(intensity.r, r);
	   assertEquals(intensity.g, g);
	   assertEquals(intensity.b, b);
	   
	   Intensity copy = new Intensity(intensity);
	   assertEquals(copy.r, intensity.r);
	   assertEquals(copy.g, intensity.g);
	   assertEquals(copy.b, intensity.b);
	   
	   Intensity falseCopy = new Intensity(1,3,2);
	   assertFalse(falseCopy.equals(copy));
	   
   } 
   
   @Test 
   public void testIpAddress() {
	   String address = "192.168.20.1";
	   IpAddress ip = new IpAddress(address);
	   assertEquals(ip.toString(), address);
	   
	   IpAddress copy = new IpAddress(ip);
	   assertTrue(copy.equals(ip));
	   
	   IpAddress falseCopy = new IpAddress("192.168.10.1");
	   assertFalse(falseCopy.equals(copy)); 
   }
   
   @Test 
   public void testObjectId( ) {
	   String ip = "192.168.20.1";
	   Username username = new Username("user");
	   Date first = new Date();
	   Timestamp time = new Timestamp(first);
	   Date second = new Date();
	   
	   UserId userId = new UserId(ip, username);
	   ObjectId objectId = new ObjectId(userId, time);
	   
	   assertEquals(objectId.toString(), userId.toString() + "_" + time.toString());
	   ObjectId falseCopy = new ObjectId(userId, new Timestamp(second));
	   assertFalse(falseCopy.equals(objectId));
   }
   
   
   @Test 
   public void testPixel() {
	   Position pos = new Position(1,2);
	   Intensity intensity = new Intensity(1,2,3);
	   
	   Pixel pixel  = new Pixel(pos, intensity);
	   assertEquals(pixel.position, pos);
	   assertEquals(pixel.intensity, intensity);
	   
	   Pixel copy = new Pixel(pixel);
	   assertEquals(copy.position, pixel.position);
	   assertEquals(copy.intensity, pixel.intensity);
	   
	   Pixel falseCopy = new Pixel(new Position(1,2),new Intensity(2,3,1));
	   assertFalse(falseCopy.equals(copy));
   }

   @Test 
   public void testRadius() {
	   double r = 1.222;
	   Radius radius = new Radius(r);
	   // assertEquals(radius.radius, r);
	   Radius copy = new Radius(radius);
	   assertTrue(copy.equals(radius));
   }
   
   
   @Test 
   public void testTimestamp() {
	   Date first = new Date();
	   Timestamp time = new Timestamp(first);
	   Date second = new Date();
	   assertEquals(time.compareTo(new Timestamp(second)), 0);
   }
   
   @Test 
   public void testUserId() {
	   String ip = "192.168.20.1";
	   Username username = new Username("user");
	   
	   UserId userId = new UserId(ip, username);
	   assertEquals(userId.toString(), ip + '_' + username.toString());
	   assertFalse(userId.toString() == "192.180.12.1" + '_' + username.toString());
   }
   
   @Test 
   public void testUsername() {
	   String username = "user";
	   
	   Username user = new Username(username);
	   Username copy = new Username(user);
	   Username falseCopy = new Username("uesr");
	   assertEquals(user.toString(), username);
	   assertTrue(copy.equals(user));
	   assertFalse(falseCopy.equals(user));
   }
   
   
}
