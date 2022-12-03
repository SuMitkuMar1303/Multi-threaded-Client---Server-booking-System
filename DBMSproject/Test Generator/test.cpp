#include <bits/stdc++.h>
using namespace std;

int main()
{
    string name[] = {"sumit","nitin","kapil","pankaj","dileep","govind","alok","armaan"};
    fstream file2;
    string filename2 = "admin_input.txt";
    file2.open(filename2 ,ios::out);
    for(int i=1;i<=500;i++)
    {
        fstream file;
        string filename = to_string(i) + ".txt";
        file.open(filename ,ios::out);
        if(!file)
        {
            cout<<"Error in creating file!!!";
            return 0;
        }
        cout<<filename<<"  "<<"File created successfully.\n";

        string s;
        string a="";
        for(int j=0;j<20;j++)
        {

            int num = 1 + (rand() % 20);
            s = to_string(num) + " ";
            for(int k=0;k<num-1;k++)
            {
                s+=name[rand()%8] + ", ";
            }
            s+=name[rand()%8] + " ";
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
            s+= (train + " " + date + " ");
            a = train + " " + date + " " + to_string(1+rand()%10) + " " + to_string(1+rand()%10) + " ";
            if(rand()%2)
            {
                s+="SL";
            }
            else
            {
                s+="AC";
            }
            // cout<<s<<"\n";

            file2<<a<<endl;
            file << s << endl;
        }

        s="#";
        file << s << endl;
        file.close();
    }
    cout<<filename2<<"  "<<"File created successfully.\n";
    file2.close();
}