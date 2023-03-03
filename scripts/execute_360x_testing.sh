#!/bin/sh

# Arguments: use-case actor input-folder output-folder
#   
# Functions

print_usage_and_die() {
 echo "Arguments: label use-case actor script-folder input-folder output-folder"
 echo " Label: Usually a Gazelle Test Instance number, but any label to help define output"
 echo " Use-Cases: accept decline ineligible cancel interim-note schedule schedule-no-show"
 echo " Actors:    initiator recipient"
 exit 1
}

check_arg_count() {
 if [ $# != 6 ] ; then print_usage_and_die; fi
}

construct_xml_file_name() {
 FILE="$4/$2_$3.xml"
 echo $FILE
}

check_use_case_actor() {
 SCRIPT_NAME=$( construct_xml_file_name $* )
 if [ ! -e $SCRIPT_NAME ] ; then
  echo Did not find $SCRIPT_NAME.
  echo One of the values you entered for Use-Case, Actor or Script-Folder is wrong.
  exit 1
 fi
}

execute_test() {
 SCRIPT_NAME=$( construct_xml_file_name $* )

      java -jar lib/360X.jar 360X $SCRIPT_NAME $5 $6
 echo java -jar lib/360X.jar 360X $SCRIPT_NAME $5 $6
  echo $6/test_report.xlsx

  mkdir -p /tmp/batch
  cp $6/test_report.xlsx /tmp/batch/$1_$2_$3.xlsx
}

check_args() {
 check_arg_count $*
 check_use_case_actor $*
}

check_args $*
execute_test $*
