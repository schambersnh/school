/*Stephen Chambers
Writing file for testing
*/

int main()
{
	//Test group for uni2utf case 1

	putchar(0x00);
	putchar(0X7F);

	putchar(0x00);
	putchar(0x05);

	putchar(0x00);
	putchar(0X00);

	putchar(0x00);
	putchar(0X6E);

	putchar(0x00);
	putchar(0X44);

	putchar(0x00);
	putchar(0X11);

	putchar(0x00);
	putchar(0X3D);

	//Test group for uni2utf case 2
	putchar(0x00);
	putchar(0x80);

	putchar(0x07);
	putchar(0XAA);

	putchar(0x05);
	putchar(0XBB);

	putchar(0x07);
	putchar(0X77);

	putchar(0x05);
	putchar(0XFF);

	putchar(0x04);
	putchar(0X88);

	putchar(0x07);
	putchar(0xFF);
	
	//test group for uni2utf case 3
	putchar(0x08);
	putchar(0x00);

	putchar(0xCA);
	putchar(0xAA);

	putchar(0xBF);
	putchar(0xAA);

	putchar(0XFF);
	putchar(0xF7);

	putchar(0xD0);
	putchar(0xFF);

	putchar(0xF8);
	putchar(0x88);

	putchar(0XFF);
	putchar(0xFF);

	
}
