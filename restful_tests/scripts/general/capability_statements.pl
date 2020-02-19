#!/usr/bin/perl

use strict;

sub print_usage_and_die {
 print
  "Arguments: input\n" .
  "           input is tab separate list of servers\n" .
  "            First column is system name\n" .
  "            Second column is URL\n";
 exit(1);
}

sub check_args {
 print_usage_and_die if (scalar(@_) != 1);
}

sub read_file {
 my ($path) = @_;
 my $handle;
 unless (open $handle, "<", $path) {
  print STDERR "Unable to open file: $path\n";
  exit(1);
 }

 my @raw_lines = <$handle>;
 my @lines;
 foreach my $line (@raw_lines) {
  $line =~ s/\r//;
  $line =~ s/\n//;
  push @lines, $line;
 }
 return @lines;
}

sub check_header {
 my ($line) = @_;

 my ($sys, $url) = split("\t", $line);
 die "In the header line, the first value is expected to be 'Sys': $line ($sys)\n" if ($sys ne "Sys");
 die "In the header line, the second value is expected to be 'URL': $line ($url)\n" if ($url ne "URL");
}

sub process_server {
 my ($line) = @_;
 my $first = substr($line, 0, 1);
 return if ($first eq "#");

 my ($sys, $url) = split("\t", $line);
 print "$sys, $url\n";
 my $x = "wget -O /tmp/capability.txt $url/metadata";
 my $y = `$x`;
}

check_args(@ARGV);
my @servers = read_file($ARGV[0]);
my $header  = shift(@servers);
check_header($header);

foreach my $server(@servers) {
 process_server($server);
}

print "Normal\n";
