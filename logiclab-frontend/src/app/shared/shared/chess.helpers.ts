import { ChessFigure } from '../../core/models/chess/logic/chessfigure';

export interface Position {
    row: number;
    col: number;
}

export function jsonToChessBoard(jsonBoard: string): (ChessFigure | null)[][] {
    const board: (ChessFigure | null)[][] = Array.from({ length: 8 }, () => Array(8).fill(null));
    
    const parsedBoard = JSON.parse(jsonBoard);
    parsedBoard.forEach((cell: any) => {
        const col = cell.column.toUpperCase().charCodeAt(0) - 'A'.charCodeAt(0); // A=0...H=7
        const row = 8 - cell.row; // Row 1 (unten) → Index 7

        if (cell.figure) {
            board[row][col] = new ChessFigure(
                cell.figure.type,
                cell.figure.figureColor,
                `${cell.column}${cell.row}`,
                cell.figure.value
            );
        } else {
            board[row][col] = null;
        }
    });

    return board;
}

export function boardToApiPosition(col: number, row: number): { convertedColumn: string, convertedRow: number } {
    const convertedColumn = String.fromCharCode('A'.charCodeAt(0) + col);
    const convertedRow = 8 - row;

    return { convertedColumn, convertedRow };
}

export function apiPositionToBoard(position: { convertedColumn: string, convertedRow: number }): { col: number, row: number } {
    const col = position.convertedColumn.toUpperCase().charCodeAt(0) - 'A'.charCodeAt(0);
    const row = 8 - position.convertedRow;
    return { col, row };
}