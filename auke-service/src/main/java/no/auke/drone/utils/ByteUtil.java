package no.auke.drone.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

//HUYDO:
//Make this Utility to pack/unpack integer values
public class ByteUtil {

    public static byte[] getBytes(boolean val) {
        return getBytes(val ? (long) 1 : (long) 0, 1);
    }
    
    public static byte[] getBytes(short val) {
        return getBytes((long) val, 2);
    }
    
    public static byte[] getBytes(int val) {
        return getBytes((long) val, 4);
    }

    public static byte[] getBytes(long val) {
        return getBytes(val, 8);
    }
   
    public static byte[] getBytes(int val, int length) {
        return getBytes((long) val, length);
    }

    public static byte[] getBytes(long val, int length) {
        
    	//if(length > 4) length =4;
        
    	byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            //shift each 8 bits, then add to the array
            result[i] = (byte) (val >> (length - i - 1) * 8 & 0xFF);
        }
        
        return result;
    }
    
    
    public static byte[] getBytes(UUID uuid) {
        
		ByteBuffer result = ByteBuffer.wrap(new byte[16]);
		result.putLong(uuid.getMostSignificantBits());
		result.putLong(uuid.getLeastSignificantBits());
        
        return result.array();
    }
    
	public static byte[] getBytes(double val) {

	    byte[] bytes = new byte[8];
	    ByteBuffer.wrap(bytes).putDouble(val);
	    return bytes;
	} 
	
	public static double getDouble(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getDouble();
	} 
    
    public static short getShort(byte[] bytes) {
        return (short) getInt(bytes);
    }
    
    public static int getInt(byte[] bytes) {
    
    	int result = 0;
    	if(bytes!=null){

    		for (int i = 0; i < bytes.length; i++)
            {	
                result |= (bytes[i] & 0xFF) << (bytes.length - i - 1) * 8;
            }        
    		
    	}
        return result;
    }
    
    public static long getLong(byte[] bytes) {
    	
    	if(bytes!=null && bytes.length>=8)
    	{
        	ByteBuffer b = ByteBuffer.wrap(bytes);    	
        	return b.getLong();
//    		long result = 0;
//    		for (int i = 0; i < bytes.length; i++)
//            {	
//                result |= (bytes[i] & 0xFF) << (bytes.length - i - 1) * 8;
//            } 
//    		return result;
    		
    	} else {
    		return 0;
    	}
    	
    }    

    public static byte[] mergeBytes(byte[]... bytes) {
        
        int size = 0;
        for (byte[] array : bytes) {
        	if(array ==null) array = new byte[0];
            size += array.length;
        }
        byte[] result = new byte[size];
        int lastPos = 0;
        for (byte[] b : bytes) {
        	if(b ==null) b = new byte[0];
            System.arraycopy(b, 0, result, lastPos, b.length);
            lastPos += b.length;
        }
        return result;
    }
    
    public static byte[] mergeBytes(List<byte[]> bytes) {
        
        
        int size = 0;
        
        for (byte[] array : bytes) {
        	
            if(array ==null) {
        	    array = new byte[0];
        	}
            size += array.length;
        }
        
        byte[] result = new byte[size];
        int lastPos = 0;
        for (byte[] b : bytes) {
        
            if(b ==null) {
                
                b = new byte[0];
            }
            
            System.arraycopy(b, 0, result, lastPos, b.length);
            lastPos += b.length;
        }
        return result;
    }    
    
    public static List<byte[]> splitBytesWithFixedLength(byte[] bytes, int length) {
    	 int size = (int) Math.ceil((bytes.length + 0.0) / length);
    	 int[] eachSplit = new int[size];
         for (int index = 0; index < size; index++) eachSplit[index] = length;
         List<byte[]> contentSubs = ByteUtil.splitBytes(bytes, eachSplit);
		return contentSubs;
    }
    public static List<byte[]> splitBytes(byte[] bytes, int... sizeEachSplit) {
    
        LinkedList<byte[]> list = new LinkedList<byte[]>();
        int size = bytes.length;
        int index = 0;
        for (int len : sizeEachSplit) {
            if (len >= size - index) break; //len is not valid, b/c it is out of bound, just break, add the remaing
            byte[] sub = new byte[len];
            System.arraycopy(bytes, index, sub, 0, len);
            list.add(sub);
            index += len; //index is the current position.
        }
        
        //add the remaining if any
        if (index < size) {
        	
            byte[] remaining = new byte[size - index];
            System.arraycopy(bytes, index, remaining, 0, size - index);
            list.add(remaining);
        }

        return list;
    }
    
    public byte[] readBytes(InputStream inputStream) throws IOException
    {       
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream(){
        	@Override
        	public synchronized byte[] toByteArray() {
        		return buf;
        	}
        };
        int bufferSize = 1024;     
        byte[] buffer = new byte[bufferSize];    
        int len = 0;    
        while ((len = inputStream.read(buffer)) != -1) 
        {         
           byteBuffer.write(buffer, 0, len);   
        }
        inputStream.close(); // hopefully this will release some more memory
        return byteBuffer.toByteArray();
    }

    public static byte[] mergeDynamicBytesWithLength(List<byte[]> bytes)
    {
        byte[] result = new byte[0];
        for(byte[] array : bytes)
        {
            if(array ==null) {
                array = new byte[0];
            }
            
            byte[] length_data = mergeBytes(getBytes(array.length, 4), array);
            result = mergeBytes(result, length_data);
        }
        return result;
    }
    
    
    public static byte[] mergeDynamicBytesWithLength(byte[]...bytes)
    {
    	byte[] result = new byte[0];
    	for(byte[] array : bytes)
    	{
    		if(array ==null) {
    		    array = new byte[0];
    		}
    		
    		byte[] length_data = mergeBytes(getBytes(array.length, 4), array);
    		result = mergeBytes(result, length_data);
    	}
    	return result;
    }
    
    public static List<byte[]> splitDynamicBytes(byte[] bytes)
    {
    	List<byte[]> result =  new LinkedList<byte[]>();
    	getData(result, bytes);
    	return result;
    }
    
    private static void getData(List<byte[]> result, byte[] bytes)
    {
    	//split first 4 bytes (contains the length of first data array)
        if(bytes!=null)
        {    
        	List<byte[]> lengthOfArray_remaining = splitBytes(bytes, 4);
        	if(lengthOfArray_remaining.size()>0)
        	{    
            	int length = getInt(lengthOfArray_remaining.get(0));
            	if(length==0)
            	{
            		//add empty array
            		result.add(new byte[0]);
            		//continue if we have more data to read
            		if(lengthOfArray_remaining.size() ==2) getData(result,  lengthOfArray_remaining.get(1));
            	}
            	else
            	{
            		if(lengthOfArray_remaining.size()==2)
            		{
            			//we have the length, so we split to get data array dynamically by the length
            			List<byte[]> dataArray_remaining = splitBytes(lengthOfArray_remaining.get(1), length);
            			result.add(dataArray_remaining.get(0));
            			if(dataArray_remaining.size() == 2) getData(result, dataArray_remaining.get(1));
            		}
            	}
        	}
        }
    	
    }
    
    public static byte[] setBit(byte[] bitmap, int bitnum){
    	
    	if(bitnum<bitmap.length*8){
    	
    		int arr = bitnum / 8;
    		int bit = bitnum - (arr*8);
    		bitmap[arr] |= 1 << bit;    		
    	}
    	return bitmap;
    }
    
    public static boolean isBit(byte[] bitmap, int bitnum){
    	
    	if(bitnum<bitmap.length*8){
    	
    		int arr = bitnum / 8;
    		int pos = bitnum - (arr*8);
    		return (bitmap[arr] & (1 << pos))!=0;  
    		
    	} else {
    		
        	return false;
    	
    	}
    }

    
        
    
}