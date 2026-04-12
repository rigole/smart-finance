import { MatDialog } from "@angular/material/dialog";
import { ConfirmDialogData } from "../models/confirmDialogModel";
import { Observable } from "rxjs";
import { ConfirmDialogComponent } from "../confirm-dialog/confirm-dialog.component";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root',
})

export class ConfirmDialogService {
  constructor(private dialog: MatDialog) { }
  open(data: ConfirmDialogData): Observable<boolean> {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data,
      disableClose: true,
      panelClass: 'confirm-dialog-panel'
    });

    return dialogRef.afterClosed();
  }
}