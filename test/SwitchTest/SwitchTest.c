int main(){
    int index = 10;
    int t = 0 , v = 0;
    switch(index){
        case 1: t = 1;break;
        case 2: t = 2;break;
        case 3: t = 3;break;
        case 4: t = 4;break;
        case 5: t = 5;break;
        default: t = index;
    }
    switch(index){
        case 1: v = 1;break;
        case 2: v = 2;break;
        case 3: v = 3;break;
        case 4: v = 4;break;
        case 10: v = 10;break;
        default: v = 200;
    }
    return 0;
}