import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { reserves } from "./reserves"
import { reserveInfo } from './reserveInfo';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  messages: string[] = [];
  private reserves:reserves = this.getFromLocalStorage();
  private reservesSubject: BehaviorSubject<reserves> = new BehaviorSubject(this.reserves);

  add(message: string) {
    console.log(message);
    this.messages.push(message);
  }
  addToReserves(reserveInfo: reserveInfo):void {
    this.reserves.items.push(reserveInfo);
    this.messages.push(reserveInfo.name);
    this.setToLocalStorage();
    console.log(reserveInfo);
  }
  removeFromReserves(BusinessName: string):void {
    this.reserves.items = this.reserves.items
    .filter(item => item.name != BusinessName);
    this.messages = this.messages.filter(message => message != BusinessName);
    this.setToLocalStorage();
  }
  containReserves(BusinessName: string) {
    return this.messages.includes(BusinessName);
  }
  cleanReserves() {
    this.reserves = new reserves();
    this.setToLocalStorage();
  }
  getReservesObservable(): Observable<reserves> {
    return this.reservesSubject.asObservable();
  }

  private setToLocalStorage():void{
    const reservesJson = JSON.stringify(this.reserves);
    localStorage.setItem('reserves', reservesJson);
    this.reservesSubject.next(this.reserves);

  }
  private getFromLocalStorage(): reserves {
    const reservesJson = localStorage.getItem('reserves');
    return reservesJson ? JSON.parse(reservesJson) : new reserves();
  }

  clear() {
    this.messages = [];
  }
}
