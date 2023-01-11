import { Component, OnInit } from '@angular/core';
import { MessageService } from '../message.service';
import { reserveInfo } from 'src/app/reserveInfo';
import { reserves } from 'src/app/reserves';

@Component({
  selector: 'app-bookings',
  templateUrl: './bookings.component.html',
  styleUrls: ['./bookings.component.css']
})
export class BookingsComponent implements OnInit {

  reserves:reserves;
  Canceled = "Reservation Canceled!"

  constructor(public messageService: MessageService) {
    this.messageService.getReservesObservable().subscribe((reserves) => {
      this.reserves = reserves;
    })
   }

  ngOnInit(): void {
  }

  cancelReserve(name:string) {
    this.messageService.removeFromReserves(name);
    alert(this.Canceled);
  }

  clearAllReserves() {
    this.messageService.cleanReserves();
  }

}
