import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogModule } from '@angular/material/dialog';
import { ConfirmDialogData, ConfirmType } from '../models/confirmDialogModel';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from "@angular/material/icon";
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-confirm-dialog',
  imports: [MatDialogActions, MatButtonModule, MatDialogModule, MatIconModule, CommonModule],
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.css'
})
export class ConfirmDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData) {}

  getIcon(type?: ConfirmType): string {
  switch (type) {
    case 'danger': return 'warning';
    case 'warning': return 'help';
    default: return 'info';
  }
}

getColor(type?: ConfirmType): string {
  switch (type) {
    case 'danger': return 'warn';
    case 'warning': return 'accent';
    default: return 'primary';
  }
}
}
