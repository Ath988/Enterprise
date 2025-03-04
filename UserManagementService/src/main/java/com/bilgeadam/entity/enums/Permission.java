package com.bilgeadam.entity.enums;

import lombok.Getter;

@Getter

public enum Permission {
    ACCESS_ALL_MODULES("allPages"),
    ACCESS_PROFILE("userProfilePage"),
    ACCESS_CALENDAR_AND_CHAT("calendarAndChatPage"),
    ACCESS_CRM("crmPage"),
    ACCESS_INVENTORY_MANAGEMENT("inventoryPage"),
    ACCESS_FINANCE_AND_ACCOUNTING("financePage"),
    ACCESS_ORGANIZATION_MANAGEMENT("organizationPage"),
    ACCESS_PROJECT_MANAGEMENT("projectPage"),
    ACCESS_HUMAN_RESOURCES("hrPage"),
    TRACK_TASKS_AND_PROJECTS("tasksPage"),
    ACCESS_ANNOUNCEMENT("announcementPage"),
    ACCESS_SURVEY("surveyPage"),;


    final String permissionTitle;

    Permission(String permissionTitle){
        this.permissionTitle = permissionTitle;
    }

}