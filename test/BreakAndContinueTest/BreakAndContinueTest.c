int main(){
    int i,size=10,t = 0;
    int num[size];
    int j = 2;
    for(i = 0 ; i < size ; ++i){
        num[i] = 0;
    }
    while(t < size){
        num[t] = size-t;
        if(t == 5){
            t = 99;
        }else
            break;
        t++;
        if(t == 3)
            break;
    }
    t = 0;
    for(i = 0 ; i < size ; ++i){
        if(i == 5)
            continue;
        else
            ++j;
        t++;
    }
    t = 2;
    j = 2;
    while( t > 0){
       --t;
       if( t == 5)
           continue;
       j--;
    }
    return 0;
}