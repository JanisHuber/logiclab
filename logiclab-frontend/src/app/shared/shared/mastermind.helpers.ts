export const COLOR_OPTIONS: string[] = (['bg-red-500', 'bg-blue-500', 'bg-green-500', 'bg-yellow-500', 'bg-purple-500', 'bg-orange-500', 'bg-pink-500', 'bg-teal-500', 'bg-gray-500']);


export function guessToColors(guess: string): string[] {
    let colorArray: string[] = [];
    for (let i = 0; i < guess.length; i++) {
      colorArray.push(COLOR_OPTIONS[parseInt(guess[i]) - 1]);
    }
    return colorArray;
}