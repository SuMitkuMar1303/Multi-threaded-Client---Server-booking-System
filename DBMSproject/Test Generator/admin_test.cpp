#include <bits/stdc++.h>
using namespace std;

int main()
{
    string name[] = {"sumit","nitin","kapil","pankaj","dileep","govind","alok","armaan"};
    for(int i=1;i<=1;i++)
    {
        fstream file;
        string filename = "admin_input.txt";
        file.open(filename ,ios::out);
        if(!file)
        {
            cout<<"Error in creating file!!!";
            return 0;
        }
        cout<<filename<<"  "<<"File created successfully.";

        string s;
        for(int j=0;j<1000;j++)
        {
            string train = to_string(8000 +rand()%1000);
            string date = "2023-01-" ;
            int x= 1+rand()%29;
            if(x<10)
            {
                date +=("0"+to_string(x));
            }
            else
            {
                date +=to_string(x);
            }
            s = train + " " + date + " " + to_string(1+rand()%10) + " " + to_string(1+rand()%10) + " ";
            // cout<<s<<"\n";
            file << s << endl;
        }
        file.close();
    }
}