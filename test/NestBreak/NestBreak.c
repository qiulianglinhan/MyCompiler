int main(){
    int i,size=10,t = 0,j;
    int num[size];
    int j = 2;
    for(i = 0 ; i < size ; ++i){
        num[i] = 0;
    }
    for(i = 0 ; i < size ; i++){
        if( i == 5)
            break;
        for(j = 0 ; j < size ; j++){
            if( j == 5)
                continue;
            if( i == 2)
                break;
            t++;
        }
    }
    return 0;
}