import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { COLOR_OPTIONS } from '../../../../shared/shared/mastermind.helpers';

@Component({
  selector: 'app-code-picker',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './code-picker.component.html',
  styleUrl: './code-picker.component.css'
})
export class CodePickerComponent {

  @Output() colorsSelected: EventEmitter<string> = new EventEmitter<string>();
  
  colorOptions: string[] = COLOR_OPTIONS;
  selectedColors: number[] = [];

  selectColor(index: number, value: number) {
    this.selectedColors[index] = value + 1;
    if (this.selectedColors.filter(x => x).length == 4) {
      this.colorsSelected.emit(this.selectedColors.join(''));
    }
  }

  cssColorClassForIndex(index: number): string {
    return COLOR_OPTIONS[this.selectedColors[index] - 1];
  }
}
