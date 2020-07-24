int max(int a,int b){
    int t;
    if(a > b)
        t = a;
    else
        t = b;
    return t;
}

int main(){
    int a = 1,b=2;
    int t;
    t = max(a,b);
    return 0;
}
