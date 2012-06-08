#Perl version of the program - a quick prototype before a proper implementation in Java

use utf8;
use LWP::UserAgent;
use LWP::Simple;
use HTML::Entities;
use MP3::Tag;
use Encode;

$ua = LWP::UserAgent->new();
$ua->cookie_jar( {} );
$ua->default_header(
'Accept-Language' => 'en-US',
'Accept-Charset' => 'utf-8');
push @{ $ua->requests_redirectable }, 'POST';

my $resp = $ua->post(
	"http://login.vk.com",
	[
		'act'       => 'login',
		'q'         => '1',
		'al_frame'  => '1',
		'from_host' => 'vk.com',
		'email'     => '', #put your vk.com login email here
		'pass'      => '' #put your vk.com password here
	]
);

print 'RESPONSE: ' . $resp->status_line . "\n";

$resp = $ua->post(
	"http://vk.com/audio",
	[
		'edit' => '0',
		'gid'  => '0',
		'act'  => 'load_audios_silent',
		'al'   => '1',
		'id'   => '' #put your vk.com id (like 999876) here
	]
);

print $resp->status_line . "\n";

if ( $resp->content =~ m/\"all\"\:\[\[(.*)\]\]/ ) {
	@recs = split( /\],\[/, $1 );
	foreach $rec (@recs) {
		$rec =~
		  m/\'\d*\',\'\d*\',\'(.*?)\',\'.*?\',\'.*?\',\'(.*?)\',\'(.*?)\'.*/;
		my $artist = decode_entities(decode("cp-1251", $2)); # decode_entities($2);
		my $title  = decode_entities(decode("cp-1251", $3)); # decode_entities($3);
        print $1 . " " . $artist . " " . $title . "\n";
		$f = '/Users/mac/Music/vk/' . $artist . ' - ' . $title . '.mp3';
		getstore( $1, $f );
		$mp3 = MP3::Tag->new( $f );
		if ($mp3) {
			# read an existing tag
			$mp3->get_tags();

			my $tag;
			if ( exists $mp3->{ID3v2} ) {
				print "Found ID3v2 tag\n";
				$tag = $mp3->{ID3v2};
			} elsif ( exists $mp3->{ID3v1} ) {
				print "Found ID3v1 tag\n";
				$tag = $mp3->{ID3v1};
			}
			if ($tag) {
				if ( !$tag->title ) {
					eval {
                        print "No title found, setting [" . $title . "]\n";
                        $tag->title($title);
                        $tag->write_tag();  
                    };
				}
				if ( !$tag->artist ) {
                    eval {
                        
                        print "No artist found, setting [" . $artist . "]\n";
                        $tag->artist($artist);
                        $tag->write_tag();
                    };
				}
			}
		}
	}

	#print map {$_,"\n"} @recs;
}
