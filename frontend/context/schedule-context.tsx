// this is fucked up im so sorry
export interface ScheduleItem {
    id: string;
    drugName: string;
    timeOfDay: string;
    quantity: string;
    dosage: string;
  }
  
  export let scheduleItems: ScheduleItem[] = [];
  
  export function addScheduleItem(item: ScheduleItem) {
    scheduleItems.push(item);
  }
  
  // sobbing
  export function clearSchedule() {
    scheduleItems.length = 0;
  }
  