/*Stephen Chambers
Professor Johnson
9/15/10
This program is designed to take input from the user, in this case an odometer reading, gallons on fillup, and unit of gas, and output a total gallons and mileage at the end of the program.
*/

#include <iostream>
using namespace std;

float convert(float, char);

int main()
{
	float miles = 0.0;
	float totalMiles = 0.0;
	float prevMiles = 0.0;
	float gas = 0.0;
	float totalGallons = 0.0;
	char unit;
	cin >> miles;
	if (miles < 0 && prevMiles == 0.0)
	{
	cout << "\n";
	cout << "Total Miles : " << 0.0 << "\n";
	cout << "Total Gallons: " << 0.0 << "\n";
	cout << "Mileage: " << 0.0 << "\n";	
	return 0;
	}
	else
	{
	totalMiles = -miles;
	}
	while(miles >= 0)
	{
			
		cin >> gas >> unit;
		cout << miles << " " << gas << " " << unit << "\n";

		totalMiles += miles - prevMiles;
		if (totalMiles != 0)
		{
		totalGallons += convert(gas, unit);
		}
		prevMiles = miles;
		cin >> miles;
	}
	cout << "\n";
	cout << "Total Miles : " << totalMiles << "\n";
	cout << "Total Gallons: " << totalGallons << "\n";
	if(totalGallons == 0.0)
	{
	cout << "Mileage: " << 0.0 << " MPG" << "\n";
	}
	else
	{
	cout << "Mileage: " << totalMiles/totalGallons << " MPG" << "\n";
	}	
	return 0;
}
float convert(float gas, char unit)
{
if(unit == 'G')
{
return gas;
}
else
{
return gas / 3.785;
}
}
