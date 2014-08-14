import edu.neumont.io.Bits;


public class HuffmanCompressor {

	public byte[] compress(HuffmanTree tree, byte[] b)
	{
		//(uncompressedSize * log(uncompressedSize)) + compressedSize
		Bits bits = new Bits();
		for(int i = 0; i < b.length; i ++)
		{
			tree.fromByte(b[i], bits);
		}
		//System.out.println(bits.toString());
		//bits now contains all the Bits in the byte[].
		//Now we do the bitshifting.
		int newSize = ((bits.size() + 8) / 8);
		byte[] cb = new byte[newSize];
		for(int i = 0 ; i < cb.length; i ++)
		{
			boolean[] bools = new boolean[Math.min(8, bits.size())];
			//Get the next 8 bools from bits.
			for(int j = 0; j < bools.length; j ++)
			{
				bools[j] = bits.poll();
			}
			cb[i] = byteFromBits(bools);
		}
		return cb;
		
	}
	
	
	
	private Byte byteFromBits(boolean[] b)
	{
		//n which will always be 8 or less. Theta 1.
		byte res = 0;
		if (b.length > 8)
		{
			return null;
		}
		else
		{
			for (int i = 0; i < b.length; i ++)
			{
				//For each boolean, add that boolean value to the byte.
				//The booleans will come in 0 being the first, 1 being the second... in order. Must be written in order.
				//So the first boolean will be the first bit of the byte.
				//meaning the byte value is...
				
				//skip 0s.
				if (b[i])
				{
					byte bool = (byte)(128 >> i);
					res = (byte) (res | bool);
				}
			}
			return res;
		}
	}
	
	public byte[] decompress(HuffmanTree tree, int uncompressedLength, byte[] b)
	{
		//compressedLength + (uncompressedLength * log(compressedLength))
		//Opposite of compress. Takes the bytes from b, turns them into Bits, then tobytes them.
		Bits bits = new Bits();
		for(int i = 0; i< b.length; i ++)
		{
			bitsFromByte(bits, b[i]);
		}
		//System.out.println(bits.toString());
		//Now I have all the bits. Now just get the next bytes from the tree.
		byte[] ub = new byte[uncompressedLength];
		for(int i = 0 ; i < uncompressedLength; i ++)
		{
			ub[i] = tree.toByte(bits);
		}
		
		return ub;
	}
	
	private void bitsFromByte(Bits bits, byte b)
	{
		//Constant time.
		for(int i = 0 ; i < 8; i ++)
		{
			int mask = (int)((int)0x1 << (7-i));
			boolean is = (b & mask) > 0;
			bits.add(is);
		}
	}

}
