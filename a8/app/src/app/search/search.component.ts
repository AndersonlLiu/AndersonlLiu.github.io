import { Component, OnInit, Output, EventEmitter} from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { debounceTime, tap, switchMap, finalize, distinctUntilChanged, filter } from 'rxjs/operators';
import { DatePipe } from '@angular/common';
import { MessageService } from '../message.service';
import { reserveInfo } from 'src/app/reserveInfo';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  message: string = "Hello!"
  @Output() messageEvent = new EventEmitter<string>();


  showTable = true;
  minLengthTerm = 1;
  dis = 0;
  ifsubmitted = false;
  InputCtrl = new FormControl();
  emailInput = new FormControl();
  dateInput = new FormControl();
  hourInput = new FormControl();
  minuteInput = new FormControl();
  email = "";
  date = "";
  hour = "";
  minute = "";
  name = "";
  searchForm!: FormGroup;
  public checked = false;
  filteredInputs: any;
  isLoading = false;
  ifSelected = false;
  dataIsEmpty = true;
  Reserved : boolean;
  errorMsg!: string;
  selectedInput: any = "";
  returnData: any = "";
  detailData: any = "";
  reviewData: any = "";
  accept:boolean;
  latitude = "";
  longitude = "";
  detailShow = false;
  displaylocation = "";
  displaycategories = "";
  isopen = "";
  marker: any;
  mapOptions: google.maps.MapOptions = {};
  photos = [];
  currentDate = new Date();
  offset = this.currentDate.getTimezoneOffset();
  yourDate = new Date(this.currentDate.getTime() - (this.offset*60*1000));
  newdate = this.yourDate.toISOString().split('T')[0];
  reserveIn : reserveInfo;
  Created = "Reservation Created!";
  Canceled = "Reservation Canceled!"
  timeCreated = "";
  status: boolean;
  twitterText = "";
  displayReserve = "";



  //coordinates: google.maps.LatLng;
  //mapOptions: any;
  //marker: any;

  constructor(private formBuilder: FormBuilder, private http: HttpClient, private messageService: MessageService) {}




  onSelected() {
    this.ifSelected = true;
    this.selectedInput = this.selectedInput;
    //console.log(this.selectedInput);
  }

  shareOnFacebook(){
    const navUrl = 'https://www.facebook.com/sharer/sharer.php?u=' + 'https://github.com/knoldus/angular-facebook-twitter.git';
    window.open(navUrl , '_blank');
  }
  shareOnTwitter() {
    const navUrl =
      'https://twitter.com/intent/tweet?text=' + 'check this' +
      'https://github.com/knoldus/angular-facebook-twitter.git';
    window.open(navUrl, '_blank');
  }

  displayWith(value: any) {
    return value;
  }

  ngOnInit()  {
    this.searchForm = this.formBuilder.group({
      category: ['all', Validators.required],
      distance: '',
      location: ['', Validators.required]
  });

    this.InputCtrl.valueChanges
      .pipe(
        filter(res => {
          return res !== null && res.length >= this.minLengthTerm
        }),
        distinctUntilChanged(),
        debounceTime(1000),
        tap(() => {
          this.errorMsg = "";
          this.filteredInputs = [];
          this.isLoading = true;
        }),
        switchMap(value => this.http.get('https://my-second-project-367205.uw.r.appspot.com/yelp/search/' + value)
          .pipe(
            finalize(() => {
              this.isLoading = false
            }),
          )
        )
      )
      .subscribe((data: any) => {
        if (data['terms'] == undefined) {
          this.errorMsg = data['Error'];
          this.filteredInputs = [];
        } else {
          this.errorMsg = "";
          this.filteredInputs = [];
          for (var i = 0; i < data['categories'].length; i++) {
            var obj = data['categories'][i];
            this.filteredInputs.push(obj['title'])
          }
          for (var i = 0; i < data['terms'].length; i++) {
            var obj = data['terms'][i];
            this.filteredInputs.push(obj['text'])
          }

        }
        //console.log(this.filteredInputs);
      });
  }
  clearSelection() {
    this.selectedInput= "";
    this.detailData="";
    this.reviewData="";
    this.searchForm.reset();
    this.checked = false;
    this.isLoading = false;
    this.ifSelected = false;
    this.dataIsEmpty = false;
    this.ifsubmitted = false;
    this.filteredInputs = [];
    //console.log(this.accept);
    this.accept = false;
    //console.log(this.accept);
    this.searchForm.controls['location'].enable();
    this.showTable = true;
    this.twitterText = "";
  }
  onSelect(event) {
    if ( event.target.checked ) {
    this.checked = true;
    this.accept = true;
    this.searchForm.controls['location'].disable();
   } else {
    this.checked = false;
    this.searchForm.controls['location'].enable();
   }
  }

  onSubmit() {
    const keyword = this.InputCtrl.value;
    console.log(keyword);
    const category = this.searchForm.value.category;
    console.log(category);
    if (this.searchForm.value.distance === "") {
      this.dis = 10 * 1609.344;
    } else {
      this.dis = Number(this.searchForm.value.distance) * 1609.344;
    }
    const distance = Math.round(this.dis).toString();
    console.log(distance);
    const location = this.searchForm.value.location;
    if (this.checked){
      this.http.get<any>('https://ipinfo.io/?token=925c24435c3495').subscribe(data => {
        const l = data.loc;
        this.latitude = l.split(',')[0];
        this.longitude = l.split(',')[1];
        console.log(this.latitude);
        console.log(typeof(this.latitude));
        console.log(this.longitude);
        this.returnInfo(this.latitude, this.longitude, keyword, category, distance);
        })
    } else {
      this.http.get<any>('https://my-second-project-367205.uw.r.appspot.com/google/' + location).subscribe(data => {
        this.latitude = data.results[0].geometry.location.lat;
        this.longitude = data.results[0].geometry.location.lng;
        console.log(this.latitude);
        console.log(typeof(this.latitude));
        console.log(this.longitude);
        this.returnInfo(this.latitude, this.longitude, keyword, category, distance);
        })
    }
  }
  returnInfo(latitude: string, longitude: string, keyword: string, category: string, distance: string) {
    this.http.get<any>('https://my-second-project-367205.uw.r.appspot.com/yelp/table/' + latitude + '/' + longitude + '/' + keyword + '/' + category + '/' + distance).subscribe(data => {
            if (Object.keys(data).length !== 0){
              this.dataIsEmpty = false;
            } else {
              this.dataIsEmpty = true;
            }
            this.ifsubmitted = true;
            this.returnData = data;
            console.log(this.dataIsEmpty);
            console.log(this.returnData);

        })
  }


  displayDetail(id: string) {
    this.http.get<any>('https://my-second-project-367205.uw.r.appspot.com/yelp/' + id).subscribe(data => {
            this.showTable = false;
            this.Reserved = this.messageService.containReserves(data.name);
            this.detailData = data;
            console.log(this.detailData);
            console.log(typeof(this.detailData.hours[0].is_open_now))
            if (this.detailData.hours[0].is_open_now) {
              this.status = true;
            } else {
              this.status = false;
            }
            console.log(this.status)
            this.detailShow = true;
            console.log(this.detailData.location.display_address);
            for (let i = 0; i < this.detailData.location.display_address.length; i++) {
              console.log(this.detailData.location.display_address[i])
              this.displaylocation += this.detailData.location.display_address[i];
              this.displaylocation += " ";
            }
            console.log(this.displaylocation);
            this.displaycategories += this.detailData.categories[0].title
            for (let i = 1; i < this.detailData.categories.length; i++) {
              this.displaycategories += " | ";
              this.displaycategories += this.detailData.categories[i].title;
            }
            console.log(this.displaycategories);
            this.mapOptions = {
              center: { lat: this.detailData.coordinates.latitude, lng: this.detailData.coordinates.longitude },
              zoom : 14
           }
           this.marker = {
            pose: { lat: this.detailData.coordinates.latitude, lng: this.detailData.coordinates.longitude },
           }
           this.photos = this.detailData.photos.slice(1);
           console.log(typeof(this.detailData.hours[0].is_open_now))
           this.twitterText = "Check " + this.detailData.name + " on Yelp";


        })
    this.http.get<any>('https://my-second-project-367205.uw.r.appspot.com/yelp/reviews/' + id).subscribe(data => {
        this.reviewData = data;
        var splitted = this.reviewData.time_created.split(" ");
        this.timeCreated = splitted[0];
        console.log(this.timeCreated);


    })
        //this.coordinates = new google.maps.LatLng(this.detailData.coordinates.latitude, this.detailData.coordinates.longitude);
  }

  addToReserves () {
    this.name = this.detailData.name;
    this.email = this.emailInput.value;
    console.log(typeof(this.email));
    this.date = this.dateInput.value;
    console.log(typeof(this.date));
    this.hour = this.hourInput.value;
    console.log(typeof(this.hour));
    this.minute = this.minuteInput.value;
    console.log(typeof(this.minute));
    this.reserveIn = {name:this.name, email:this.email, date:this.date, hour:this.hour, minute:this.minute}
    this.messageService.addToReserves(this.reserveIn);
    alert(this.Created);
    this.displayReserve = 'none';
  }
  toggleDisplayReserve () {
    this.displayReserve = 'block';
  }
  cancelReserve(name:string) {
    this.messageService.removeFromReserves(name);
    alert(this.Canceled);
  }
  showTableFunc() {
    this.showTable = true;
    this.displaylocation = "";
    this.displaycategories = "";

  }
}
